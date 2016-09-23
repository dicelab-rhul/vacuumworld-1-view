$(document).ready(function() {
	$(function() {
		listenForTemplateLoadRequest();
	});
});

function listenForTemplateLoadRequest() {
	$("#load_button").on("click", function() {
		createTemplatesDialog();
		$("#load_template").dialog("open");
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

function listenForResetTemplatesRadio() {
	$('#cancel_template_upload').on("click", function() {
		$('input[name="template"]').prop('checked', false);
		$('input[id="t1"]').prop('checked', true);
		$("#load_template").dialog("destroy");
	});
}