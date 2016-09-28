var clicked = null;

$(document).ready(function() {
	$(function() {
		listenForNewSimulationRequest();
	});
});

function listenForNewSimulationRequest() {
	$("#new_simulation_button").on("click", function() {
		createGridSizeDialog();
		$("#new_simulation").dialog("open");
		listenForGridSizeOkButton();
		
		return false;
	});
}

function createGridSizeDialog() {
	$("#new_simulation").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select the grid size",
	});
}

function listenForGridSizeOkButton() {
	$("#grid_size_ok_button").on("click", function() {
		$("#new_simulation").dialog("close");
		
		if(!validateForm("grid_size","grid_size")) {
			return false;
		}
		if(!positiveInteger("grid_size", "grid_size")) {
			return false;
		}
		
		createUserAndMonitoringDialog();
		$("#new_simulation_2").dialog("open");
		listenForUserAndMonitoringOkButton();
		
		return false;
	});
}

function createUserAndMonitoringDialog() {
	$("#new_simulation_2").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Select your choices",
	});
}

function listenForUserAndMonitoringOkButton() {
	$("#user_and_monitoring_ok_button").on("click", function() {
		$("#new_simulation_2").dialog("close");
		
		var size = document.forms["grid_size"]["grid_size"].value;
		createGridDialog(size);
		populateGridDialog(size);
		
		$("#dialog_grid").append("<br />");
		$("#new_simulation_3").dialog("open");
		listenForAddAgentsOkButton();
		
		return false;
	});
}

function createGridDialog(size) {
	$("#new_simulation_3").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Add agents and pieces of dirt",
		maxWidth: Math.max(60 + 53 * size, 500),
        width: Math.max(60 + 53 * size, 500),
	});
}

function populateGridDialog(size) {
	$("#dialog_grid").empty();
	
	createAddAgentOrDirtDialog();
	
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			var x = i+1, y = j+1;
			var id = "image_" + x + "_" + y;
			$("#dialog_grid").append("<img id=\"" + id + "\" class=\"grid_image\" src=\"images/location.png\" style=\"border: 1px solid #000000;\"/>");
			addClickListener(id);
		}
		$("#dialog_grid").append("<br />");
	}
}

function addClickListener(id) {
	$("#" + id).on("click", function() {
		clicked = id;
		$("#new_simulation_4").dialog("open");
		listenForAddAgentOrDirt(id);
		listenForCancelAddAgentOrDirt();
	});
}

function createAddAgentOrDirtDialog() {
	if(!$("#new_simulation_4").hasClass('ui-dialog-content')) {
		$("#new_simulation_4").dialog({
			autoOpen: false,
			modal: true,
			dialogClass: "no-close",
			title: "Choose an agent or dirt type",
			width: "400",
		});
	}
}

function listenForAddAgentOrDirt(id) {
	$("#add_agent_or_dirt_button").on("click", function() {
		if(clicked === id) {
			var choice = $('input:radio[name=agent_or_dirt]:checked').val();
			var newImage = getNewImagePath(choice);
			$("#" + id).attr("src", newImage);
			closeAgentOrDirtSelectionDialog();
			clicked = null;
		}
		
		return false;
	});
}

function getNewImagePath(choice) {
	return "images/" + choice + ".png"
}

function closeAgentOrDirtSelectionDialog() {
	$("#new_simulation_4").dialog("destroy");
	createAddAgentOrDirtDialog();
}

function listenForCancelAddAgentOrDirt() {
	$("#cancel_add_agent_or_dirt").on("click", function() {
		closeAgentOrDirtSelectionDialog();
		clicked = null;
		
		return false;
	})
}

function listenForAddAgentsOkButton() {
	$("#add_agents_ok_button").on("click", function() {
		$("#new_simulation_3").dialog("close");
		
		createNewTemplateRecapDialog();
		$("#new_simulation_5").dialog("open");
		listenForRecapOkButton();
		listenForRecapSaveButton();
		
		return false;
	});
}

function createNewTemplateRecapDialog() {
	$("#new_simulation_5").dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Are you satisfied with your choices?",
		width: 500,
	});
}

function listenForRecapOkButton() {
	$("#recap_ok_button").on("click", function() {
		$("#new_simulation_5").dialog("close");
		
		collectDataAndStartSystem();
		
		return false;
	});
}

function listenForRecapSaveButton() {
	$("#recap_save_button").on("click", function() {
		//TODO save the template
		
		return false;
	});
}

function resetNewTemplateSelection() {
	$("#dialog_grid").empty();
	
	document.getElementById("grid_size_form").reset();
	
	$("#new_simulation").dialog("destroy");
	$("#new_simulation_2").dialog("destroy");
	$("#new_simulation_3").dialog("destroy");
	
	if ($('#new_simulation_4').hasClass('ui-dialog-content')) {
		$("#new_simulation_4").dialog("destroy");
	}

	$("#new_simulation_5").dialog("destroy");
}

function collectDataAndStartSystem() {
	var size = document.forms["grid_size"]["grid_size"].value;
	var userPresent = $("#user_present").attr('checked') === "checked" ? "yes" : "no";
	var monitoring = $("#monitoring_active").attr('checked') === "checked" ? "yes" : "no";
	
	var locations = buildNotableLocations(size);
	//alert(locations);
	var initialState = [size, userPresent, monitoring, locations];
	
	//var serializedInitialState = $.serialize(initialState);
	
	$.post("start", {INITIAL:initialState}, function(data) {
		//var splitted = data.split("|");
		
		/*for(var i=1; i < splitted.length; i++) {
			alert(splitted[i]);
		}*/
		//window.location = splitted[0];
		window.location = data;
	});
}

function buildNotableLocations(size) {
	var locations = "";
	
	for(var i = 0; i < size; i++) {		
		for(var j = 0; j < size; j++) {
			var x = i+1, y = j+1;
			var id = "image_" + x + "_" + y;
			
			var candidate = buildLocationIfNecessary(x, y, id);
			
			if(candidate != null) {
				//alert(candidate);
				locations += (locations === "" ? candidate : "#" + candidate);
			}
		}
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
		//alert("not matched dirt");
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
	//return [x, y, ["agent", color, facing]];
	return "begin_location|" + x + "|" + y + "|" + "agent" + "|" + color + "|" + facing + "|end_location";
}

function createNotableLocationWithDirt(x, y, name) {
	if(name.match("green")) {
		//return [x, y, ["dirt", "green"]];
		return "begin_location|" + x + "|" + y + "|dirt|green|end_location";
	}
	else if(name.match("orange")) {
		//return [x, y, ["dirt", "orange"]];
		return "begin_location|" + x + "|" + y + "|dirt|orange|end_location";
	}
	else {
		return null;
	}
}