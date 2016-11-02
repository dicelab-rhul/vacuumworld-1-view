var initialState = [];
var gridSizeDialog = null;
var userDialog = null;
var gridDialog = null;
var addBodyDialog = null;
var recapDialog = null;

$(document).ready(setupEnvironment);

function setupEnvironment() {
	listenForNewSimulationButton();
}

function listenForNewSimulationButton() {
	$("#new_simulation_button").on("click", manageClickOnNewSimulationButton);
}

function manageClickOnNewSimulationButton() {
	initialState = [];
	destroyLeftovers();
	gridSizeDialog = createGridSizeDialog();
	gridSizeDialog.dialog("open");
	listenToGridSizeOkButton();
	listenToGridSizeCancelButton();
}

function destroyLeftovers() {
	gridSizeDialog = null;
	userDialog = null;
	gridDialog = null;
	addBodyDialog = null;
	recapDialog = null;
}

function createGridSizeDialog() {
	var localGridSizeDiv = createGridSizeDiv();
	
	return localGridSizeDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select the grid size",
	});
}

function createGridSizeDiv() {
	var localGridSizeDiv = $("<div id=\"select_size_dialog\" class=\"dialog\" style=\"text-align: center;margin: auto;\">");
	
	localGridSizeDiv.append("<form name=\"grid_size_form\" id=\"grid_size_form\" action=\"\" method=\"post\">");
	localGridSizeDiv.append("<input type=\"text\" name=\"grid_size\" id=\"grid_size\" /><br /> <br />");
	localGridSizeDiv.append("<input type=\"submit\" value=\"Next\" id=\"grid_size_ok_button\"> ");
	localGridSizeDiv.append("<input type=\"submit\" value=\"Cancel\" id=\"grid_size_cancel_button\">");
	localGridSizeDiv.append("</form>");
	localGridSizeDiv.append("</div>");
	
	return localGridSizeDiv;
}

function listenToGridSizeOkButton() {
	$("#grid_size_ok_button").on("click", manageClickOnGridSizeOkButton);
}

function manageClickOnGridSizeOkButton() {	
	if(!validateForm($("#grid_size").val())) {
		showError("The size field cannot be empty!!!");
		gridSizeDialog.dialog("destroy");
		gridSizeDialog = null;
		
		return;
	}
	else if(!positiveInteger($("#grid_size").val())) {
		showError("The size must be an integer > 0 and <= 10");
		gridSizeDialog.dialog("destroy");
		gridSizeDialog = null;
		
		return;
	}
	else {
		initialState[0] = $("#grid_size").val();
		gridSizeDialog.dialog("destroy");
		gridSizeDialog = null;
		createAndShowUserDialog();
	}
}

function listenToGridSizeCancelButton() {
	$("#grid_size_ok_button").on("click", manageClickOnGridSizeCancelButton);
}

function manageClickOnGridSizeCancelButton() {
	gridSizeDialog.dialog("destroy");
	gridSizeDialog = null;
}

function createAndShowUserDialog() {
	userDialog = createUserDialog();
	userDialog.dialog("open");
	listenToUserOkButton();
	listenToUserCancelButton();
}

function createUserDialog() {
	var userDiv = createUserDiv();
	
	return userDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select your choices",
	});
}

function createUserDiv() {
	var userDiv = $("<div id=\"user_choice_dialog\" class=\"dialog\" style=\"text-align: center;margin: auto;\">");
	
	userDiv.append("<form name=\"user_choice\" id=\"user_choice\" action=\"\" method=\"post\">");
	userDiv.append("<input type=\"checkbox\" id=\"user_present\" name=\"user_present\" value=\"t1\" checked=\"checked\"/> Add random walking user<br /><br />");
	userDiv.append("<input type=\"submit\" value=\"Next\" id=\"user_ok_button\"> ");
	userDiv.append("<input type=\"submit\" value=\"Cancel\" id=\"user_cancel_button\">");
	userDiv.append("</form>");
	userDiv.append("</div>");
	
	return userDiv;
}

function listenToUserOkButton() {
	$("#user_ok_button").on("click", manageClickOnUserOkButton);
}

function manageClickOnUserOkButton() {
	initialState[1] = $("#user_present").prop("checked") ? "yes" : "no";
	userDialog.dialog("destroy");
	userDialog = null;
	createAndShowGridDialog();
}

function listenToUserCancelButton() {
	$("#user_cancel_button").on("click", manageClickOnUserCancelButton);
}

function manageClickOnUserCancelButton() {
	userDialog.dialog("destroy");
	userDialog = null;
}

function createAndShowGridDialog() {
	var size = initialState[0];
	
	gridDialog = createGridDialog(size);
	addListenersToLocations(size);
	gridDialog.dialog("open");
	listenToGridOkButton();
	listenToGridCancelButton();
}

function createGridDialog(size) {
	var gridDiv = createGridDiv(size);
	
	return gridDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Add agents and pieces of dirt",
		maxWidth: Math.max(60 + 53 * size, 500),
        width: Math.max(60 + 53 * size, 500),
	});
}

function createGridDiv(size) {
	var gridDiv = $("<div id=\"add_agents_or_dirts_dialog\" class=\"dialog\" style=\"text-align: center;margin: auto;\">");
	
	gridDiv.append("<div id=\"dialog_grid\" class=\"centered_div\" style=\"text-align: center;margin: auto;\">");
	var squares = generateGridSquares(size);
	gridDiv.append(squares);
	gridDiv.append("</div>");
	gridDiv.append("<form name=\"add_agents_or_dirts\" id=\"add_agents_or_dirts\" action=\"\" method=\"post\">");
	gridDiv.append("<div class=\"centered_div\" style=\"text-align: center;margin: auto;\">");
	gridDiv.append("<input type=\"submit\" value=\"Next\" id=\"grid_ok_button\"> ");
	gridDiv.append("<input type=\"submit\" value=\"Cancel\" id=\"grid_cancel_button\">");
	gridDiv.append("</div>");
	gridDiv.append("</form>");
	gridDiv.append("</div>");
	
	return gridDiv;
}

function generateGridSquares(size) {
	var squares = "";
	
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			var y = i+1, x = j+1;
			var id = "image_" + x + "_" + y;
			squares += "<img id=\"" + id + "\" class=\"grid_image\" src=\"images/location.png\" />";
		}
		
		squares += "<br />";
	}
	
	squares += "<br />";
	
	return squares;
}

function addListenersToLocations(size) {
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			var y = i+1, x = j+1;
			var id = "image_" + x + "_" + y;
			$("#" + id).on("click", {id: id}, manageClickOnLocationImage);
		}
	}
}

function manageClickOnLocationImage(event) {
	var id = event.data.id;
	
	addBodyDialog = createAddBodyDialog();
	addBodyDialog.dialog("open");
	listenForAddBodyOkButton(id);
	listenForAddBodyCancelButton();
}

function createAddBodyDialog() {
	var addBodyDiv = createAddBodyDiv();
	
	return addBodyDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close", 
		title: "Choose an agent or dirt type",
		width: "400",
	});
}

function createAddBodyDiv() {
	var addBodyDiv = $("<div class=\"centered_div\" id=\"add_agent_or_dirt_dialog\" style=\"text-align: center;margin: auto;\">");
	
	addBodyDiv.append("<form action=\"\" method=\"post\">");
	addBodyDiv.append("<div id=\"agent_or_dirt_choices\" class=\"radio_choices\" style=\"text-align: left;margin: auto;\">");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"green_north\" checked=\"checked\" /> Green agent facing North<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"green_south\" /> Green agent facing South<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"green_west\" /> Green agent facing West<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"green_east\" /> Green agent facing East<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"orange_north\" /> Orange agent facing North<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"orange_south\" /> Orange agent facing South<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"orange_west\" /> Orange agent facing West<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"orange_east\" /> Orange agent facing East<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"white_north\" /> White agent facing North<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"white_south\" /> White agent facing South<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"white_west\" /> White agent facing West<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"white_east\" /> White agent facing East<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"green_dirt\" /> Green dirt<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"orange_dirt\" /> Orange dirt<br />");
	addBodyDiv.append("<input type=\"radio\" name=\"agent_or_dirt\" value=\"location\" /> Empty location<br />");
	addBodyDiv.append("<br />");
	addBodyDiv.append("</div>");
	addBodyDiv.append("<div class=\"centered_div\" style=\"text-align: center;margin: auto;\">");
	addBodyDiv.append("<input type=\"button\" value=\"Add\" id=\"add_body_ok_button\"/> ");
	addBodyDiv.append("<input type=\"button\" value=\"Cancel\" id=\"add_body_cancel_button\"/>");
	addBodyDiv.append("</div>");
	addBodyDiv.append("</form>");
	addBodyDiv.append("</div>");
	
	return addBodyDiv;
}

function listenForAddBodyOkButton(id) {
	$("#add_body_ok_button").on("click", {id: id}, manageClickOnAddBodyOkButton);
}

function manageClickOnAddBodyOkButton(event) {
	var choice = $('input:radio[name=agent_or_dirt]:checked').val();
	var newImage = "images/" + choice + ".png";
	$("#" + event.data.id).attr("src", newImage);
	
	addBodyDialog.dialog("destroy");
	addBodyDialog = null;
}

function listenForAddBodyCancelButton() {
	$("#add_body_cancel_button").on("click", manageClickOnAddBodyCancelButton);
}

function manageClickOnAddBodyCancelButton() {
	addBodyDialog.dialog("destroy");
	addBodyDialog = null;
}

function noAgents(size) {	
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

function listenToGridOkButton() {
	$("#grid_ok_button").on("click", manageClickOnGridOkButton);
}

function manageClickOnGridOkButton() {
	var size = initialState[0];
	
	if(noAgents(size)) {
		showError("Add at least one agent!!!");
	}
	else {
		initialState[2] = buildNotableLocations(initialState[0]);
		gridDialog.dialog("destroy");
		gridDialog = null;
		createAndShowRecapDialog();
	}
}

function listenToGridCancelButton() {
	$("#grid_cancel_button").on("click", manageClickOnGridCancelButton);
}

function manageClickOnGridCancelButton() {
	gridDialog.dialog("destroy");
	gridDialog = null;
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

function createAndShowRecapDialog() {
	recapDialog = createRecapDialog();
	recapDialog.dialog("open");
	listenToRecapOkButton();
	listenToRecapCancelButton();
}

function createRecapDialog() {
	var recapDiv = createRecapDiv();
	
	return recapDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Are you satisfied with your choices?",
		width: 500,
	});
}

function createRecapDiv() {
	var recapDiv = $("<div id=\"recap_dialog\" class=\"dialog\" style=\"text-align: center;margin: auto;\">");
	
	recapDiv.append("<form action=\"\" method=\"post\">");
	recapDiv.append("<div class=\"centered_div\" style=\"text-align: center;margin: auto;\">");
	recapDiv.append("<p>OK to proceed, Cancel to abort.</p>");
	recapDiv.append("<input type=\"button\" value=\"Ok\" id=\"recap_ok_button\" /> ");
	recapDiv.append("<input type=\"button\" value=\"Cancel\" id=\"recap_cancel_button\"/>");
	recapDiv.append("</div>");
	recapDiv.append("</form>");
	recapDiv.append("</div>");
	
	return recapDiv;
}

function listenToRecapOkButton() {
	$("#recap_ok_button").on("click", manageClickOnRecapOkButton);
}

function manageClickOnRecapOkButton() {
	recapDialog.dialog("destroy");
	recapDialog = null;
	startSystem();
}

function startSystem() {
	$.post("start", {INITIAL:initialState}, function(data) {
		window.location = data;
	});
}

function listenToRecapCancelButton() {
	$("#recap_cancel_button").on("click", manageClickOnRecapCancelButton);
}

function manageClickOnRecapCancelButton() {
	recapDialog.dialog("destroy");
	recapDialog = null;
}














