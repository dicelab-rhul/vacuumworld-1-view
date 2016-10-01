$(document).ready(function() {
	$(function() {
		listenForTemplateLoadRequest();
	});
});

function listenForTemplateLoadRequest() {
	$("#load_button").on("click", function() {
		createTemplatesDialog();
		$("#load_template").dialog("open");
		listenForLoadTemplateConfirmation();
		listenForResetTemplatesRadio();
	});
}

function createTemplatesDialog() {
	$("#load_template").dialog({
		autoOpen : false,
		modal : true,
		dialogClass : "no-close",
		title : "Select a template from the list",
		maxWidth : 800,
		width : 800,
	});
}

function listenForLoadTemplateConfirmation() {
	$('#load_template_ok_button').on("click", function() {
		var choice = $('input:radio[name=template]:checked').val();
		var initialState = createTemplateToLoad(choice);
		$("#load_template").dialog("destroy");
		
		doFirstRequest(initialState);
	});
}

function listenForResetTemplatesRadio() {
	$('#cancel_template_upload').on("click", function() {
		$('input[name="template"]').prop('checked', false);
		$('input[id="t1"]').prop('checked', true);
		$("#load_template").dialog("destroy");
	});
}

function doFirstRequest(initialState) {
	if(initialState != null) {
		$.post("start", {INITIAL:initialState}, function(data) {
			window.location = data;
		});
	}
}

function createTemplateToLoad(choice) {
	switch(choice) {
	case "t1":
		return ["3", "no", "no", "begin_location|1|2|dirt|green|end_location#begin_location|2|1|agent|green|east|end_location"];
	case "t2":
		return ["3", "no", "no", "begin_location|1|1|dirt|orange|end_location#begin_location|2|2|agent|orange|north|end_location"];
	case "t3":
		return ["5", "no", "no", "begin_location|4|3|dirt|green|end_location#begin_location|1|2|agent|green|east|end_location"];
	case "t4":
		return ["5", "no", "no", "begin_location|3|2|dirt|orange|end_location#begin_location|1|4|agent|orange|north|end_location"];
	case "t5":
		return ["10", "no", "no", "begin_location|9|5|dirt|green|end_location#begin_location|4|4|agent|green|north|end_location#begin_location|7|6|dirt|orange|end_location#begin_location|1|8|agent|orange|north|end_location"];
	default:
		return null;
	}
}