var clicked = null;

$(document).ready(setupEnvironment);

function setupEnvironment() {
	listenForNewSimulationButton();
}

function createErrorDialog() {
	createErrorDialogHelper();
	listenForClickOnErrorDialogOkButton();
}

function listenForClickOnErrorDialogOkButton() {
	$("#error_ok_button").on("click", manageClickOnErrorOkButton);
}

function manageClickOnErrorOkButton() {
	$("#error_dialog").empty();
	$("#error_dialog").remove();
}

function destroyDialogIfPresent(id) {
	if($(id).hasClass('ui-dialog-content')) {
		$(id).setVisible(false);
		$(id).dialog("destroy");
	}
}

function createErrorDialogHelper() {	
	var errorDiv = createErrorDiv();
	createDialogFromErrorDiv(errorDiv);
}

function createErrorDiv() {
	var errorDiv = $("<div id=\"error_dialog\" class=\"dialog\">");
	
	errorDiv.append("<form action=\"\" method=\"post\"></form>");
	errorDiv.append("<div class=\"centered_div\">");
	errorDiv.append("<p id=\"error_message\"></p>");
	errorDiv.append("<input type=\"submit\" value=\"Ok\" id=\"error_ok_button\">");
	errorDiv.append("</div>");
	errorDiv.append("</form>");
	errorDiv.append("</div>");
	
	return errorDiv;
}

function createDialogFromErrorDiv(errorDiv) {
	errorDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Error"
	});
}

function listenForNewSimulationButton() {
	$("#new_simulation_button").on("click", createAndShowGridSizeDialog);
}

function createAndShowGridSizeDialog() {
	createGridSizeDialog();
	$("#select_size_dialog").dialog("open");
	
	return false;
}

function createGridSizeDialog() {
	//destroyDialogIfPresent("#select_size_dialog");
	createGridSizeDialogHelper();
	listenForGridSizeOkButton();
	listenForGridSizeCancelButton();
}

function listenForGridSizeCancelButton() {
	$("#grid_size_cancel_button").on("click", manageClickOnGridSizeCancelButton);
}

function manageClickOnGridSizeCancelButton() {
	destroyDialogIfPresent("#select_size_dialog");
}

function createGridSizeDialogHelper() {
	$("#select_size_dialog").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select the grid size",
	});
}

function listenForGridSizeOkButton() {
	$("#grid_size_ok_button").on("click", manageClickOnGridSizeOkButton);
}

function manageClickOnGridSizeOkButton() {
	$("#select_size_dialog").dialog("close");
	
	if(!validateForm("grid_size", "grid_size")) {
		manageEmptySizeTextboxError();
		
		return false;
	}
	else if(!positiveInteger("grid_size", "grid_size")) {
		manageInvalidContentInSizeTextboxError();
		
		return false;
	}
	else {
		createUserAndMonitoringDialog();
		$("#user_choice_dialog").dialog("open");
		
		return false;
	}
}

function manageEmptySizeTextboxError() {
	showError("The size must be an integer > 0 and <= 10");
}

function manageInvalidContentInSizeTextboxError() {
	showError("The size must be an integer > 0 and <= 10");
}

function showError(text) {
	createErrorDialog();
	$("#error_message").html(text);
	$("#error_dialog").dialog("open");
	
	return false;
}

function createUserAndMonitoringDialog() {
	destroyDialogIfPresent("#user_choice_dialog");
	createUserAndMonitoringDialogHelper();
	listenForUserAndMonitoringOkButton();
	listenForUserAndMonitoringCancelButton();
}

function listenForUserAndMonitoringCancelButton() {
	$("#user_and_monitoring_cancel_button").on("click", manageClickOnUserAndMonitoringCancelButton);
}

function manageClickOnUserAndMonitoringCancelButton() {
	destroyDialogIfPresent("#select_size_dialog");
	destroyDialogIfPresent("#user_choice_dialog");
}

function createUserAndMonitoringDialogHelper() {
	$("#user_choice_dialog").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select your choices",
	});
}

function listenForUserAndMonitoringOkButton() {
	$("#user_and_monitoring_ok_button").on("click", manageClickOnUserAndMonitoringOkButton);
}

function manageClickOnUserAndMonitoringOkButton() {
	$("#user_choice_dialog").dialog("close");
	var size = document.forms["grid_size"]["grid_size"].value;
	createGridDialog(size);
	populateGridDialog(size);
	$("#add_agents_or_dirts_dialog").dialog("open");
	
	return false;
}

function createGridDialog(size) {
	destroyDialogIfPresent("#add_agents_or_dirts_dialog");
	createGridDialogHelper(size);
	listenForAddAgentsOkButton();
	listenForAddAgentsCancelButton();
	
	return size;
}

function createGridDialogHelper(size) {
	$("#add_agents_or_dirts_dialog").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Add agents and pieces of dirt",
		maxWidth: Math.max(60 + 53 * size, 500),
        width: Math.max(60 + 53 * size, 500),
	});
}

function populateGridDialog(size) {
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			setupLocationImage(i, j);
		}
		
		$("#dialog_grid").append("<br />");
	}
	
	$("#dialog_grid").append("<br />");
}

function setupLocationImage(i, j) {
	var y = i+1, x = j+1;
	var id = "image_" + x + "_" + y;
	$("#dialog_grid").append("<img id=\"" + id + "\" class=\"grid_image\" src=\"images/location.png\" />");
	$("#" + id).on("click", {id: id}, manageClickOnLocationImage);
}

function manageClickOnLocationImage(event) {
	clicked = event.data.id;
	resumeOrRecreateAddAgentOrDirtDialog();
	listenForAddAgentOrDirtOkButton(event.data.id);
	listenForAddAgentOrDirtCancelButton();
	
	return false;
}

function resumeOrRecreateAddAgentOrDirtDialog() {
	if(!$("#add_agent_or_dirt").hasClass('ui-dialog-content')) {
		createAddAgentOrDirtDialog();
	}
	
	$("#add_agent_or_dirt_dialog").dialog("open");
}

function createAddAgentOrDirtDialog() {
	$("#add_agent_or_dirt_dialog").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Choose an agent or dirt type",
		width: "400",
	});
}

function listenForAddAgentOrDirtOkButton(id) {
	$("#add_agent_or_dirt_ok_button").on("click", {id: id}, manageClickOnAddAgentOrDirtOkButton);
}

function manageClickOnAddAgentOrDirtOkButton(event) {
	if(clicked === event.data.id) {
		var choice = $('input:radio[name=agent_or_dirt]:checked').val();
		var newImage = "images/" + choice + ".png";
		$("#" + event.data.id).attr("src", newImage);
		clicked = null;
	}
	
	$("#add_agent_or_dirt_dialog").dialog("close");
	
	return false;
}

function listenForAddAgentOrDirtCancelButton() {
	$("#add_agent_or_dirt_cancel_button").on("click", manageClickOnAddAgentOrDirtCancelButton);
}

function manageClickOnAddAgentOrDirtCancelButton() {
	$("#add_agent_or_dirt_dialog").dialog("close");
	clicked = null;
	
	return false;
}

function listenForAddAgentsOkButton() {
	$("#add_agents_ok_button").on("click", manageClickOnAddAgentsOkButton);
}

function listenForAddAgentsCancelButton() {
	$("#add_agents_cancel_button").on("click", manageClickOnAddAgentsCancelButton);
}

function manageClickOnAddAgentsCancelButton() {
	destroyDialogIfPresent("#select_size_dialog");
	destroyDialogIfPresent("#user_choice_dialog");
	destroyDialogIfPresent("#add_agents_or_dirt_dialog");
}

function manageClickOnAddAgentsOkButton() {
	if(noAgents()) {
		manageNoAgentsAddedError();
		
		return false;
	}
	else {
		$("#add_agents_or_dirts_dialog").dialog("close");
		createNewTemplateRecapDialog();
		$("#recap_dialog").dialog("open");
		
		return false;
	}
}

function noAgents() {
	var size = document.forms["grid_size"]["grid_size"].value;
	
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			var y = i+1, x = j+1;
			var id = "image_" + x + "_" + y;
			var src = $("#" + id).attr("src");
			
			if(src != "images/location.png" && src != "images/green_dirt.png" && src != "images/orange_dirt.png" && src != "images/neutral_dirt.png") {
				return false;
			}
		}
	}
	
	return true;
}

function manageNoAgentsAddedError() {
	showError("Add at least one agent!!!");
	
	return false;
}

function createNewTemplateRecapDialog() {
	destroyDialogIfPresent("#recap_dialog");
	createNewTemplateRecapDialogHelper();
	listenForRecapOkButton();
	listenForRecapSaveButton();
	listenForRecapCancelButton();
}

function createNewTemplateRecapDialogHelper() {
	$("#recap_dialog").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Are you satisfied with your choices?",
		width: 500,
	});
}

function listenForRecapOkButton() {
	$("#recap_ok_button").on("click", manageClickOnRecapOkButton);
}

function listenForRecapSaveButton() {
	$("#recap_save_button").on("click", manageClickOnRecapSaveButton);
}

function listenForRecapCancelButton() {
	$("#recap_cancel_button").on("click", manageClickOnRecapCancelButton);
}

function manageClickOnRecapCancelButton() {
	destroyDialogIfPresent("#select_size_dialog");
	destroyDialogIfPresent("#user_choice_dialog");
	destroyDialogIfPresent("#add_agents_or_dirt_dialog");
	destroyDialogIfPresent("#add_agent_or_dirt_dialog");
	destroyDialogIfPresent("#recap_dialog");
}

function manageClickOnRecapSaveButton() {
	showError("Not yet implemented!!!");
}

function manageClickOnRecapOkButton() {
	$("#recap_dialog").dialog("close");
	collectDataAndStartSystem();
	
	return false;
}

function collectDataAndStartSystem() {
	var initialState = collectData();
	
	$.post("start", {INITIAL:initialState}, function(data) {
		window.location = data;
	});
}

function collectData() {
	var size = document.forms["grid_size"]["grid_size"].value;
	var userPresent = $("#user_present").prop("checked") ? "yes" : "no";
	var monitoring = $("#monitoring_active").prop("checked") ? "yes" : "no";
	
	var locations = buildNotableLocations(size);
	return [size, userPresent, monitoring, locations];
}

function buildNotableLocations(size) {
	var locations = "";
	
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			locations = buildNotableLocationIfNecessary(i, j, locations);
		}
	}
	
	return locations;
}

function buildNotableLocationIfNecessary(i, j, locations) {
	var x = i+1, y = j+1;
	var id = "image_" + x + "_" + y;
	
	var candidate = buildLocationIfNecessary(x, y, id);
	
	if(candidate != null) {
		locations += (locations === "" ? candidate : "#" + candidate);
	}
	
	return locations;
}

function buildLocationIfNecessary(x, y, id) {
	if($("#" + id).attr("src") != "images/location.png") {
		return createNotableLocation(x, y, $("#" + id).attr("src"));
	}
	else {
		return null;
	}
}

function createNotableLocation(x, y, image_path) {
	var name = image_path.split("/")[1];
	
	if(!(name.match("dirt"))) {
		return createNotableLocationWithAgent(x, y, name);
	}
	else {
		return createNotableLocationWithDirt(x, y, name);
	}
}

function createNotableLocationWithAgent(x, y, name) {
	if(name.match("green")) {
		return createNotableLocationWithAgentHelper(x, y, "green", name);
	}
	else if(name.match("orange")) {
		return createNotableLocationWithAgentHelper(x, y, "orange", name);
	}
	else if(name.match("white")) {
		return createNotableLocationWithAgentHelper(x, y, "white", name);
	}
	else {
		return null;
	}
}

function createNotableLocationWithAgentHelper(x, y, color, name) {
	if(name.match("north")) {
		return packLocationWithAgent(x, y, color, "north");
	}
	else if(name.match("south")) {
		return packLocationWithAgent(x, y, color, "south");
	}
	else if(name.match("west")) {
		return packLocationWithAgent(x, y, color, "west");
	}
	else if(name.match("east")) {
		return packLocationWithAgent(x, y, color, "east");
	}
	else {
		return null;
	}
}

function packLocationWithAgent(x, y, color, facing) {
	return "begin_location|" + x + "|" + y + "|" + "agent" + "|" + color + "|" + facing + "|end_location";
}

function createNotableLocationWithDirt(x, y, name) {
	if(name.match("green")) {
		return "begin_location|" + x + "|" + y + "|dirt|green|end_location";
	}
	else if(name.match("orange")) {
		return "begin_location|" + x + "|" + y + "|dirt|orange|end_location";
	}
	else {
		return null;
	}
}