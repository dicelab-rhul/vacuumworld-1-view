$(document).ready(function() {
	$(function() {
		listenForFileLoadRequest();
	});
});

function listenForFileLoadRequest() {
	$("#load_file_button").on("click", function() {
		createLoadFileDialog();
		
		$("#load_file").dialog("open");
		
		listenForFileUpload();
		listenForResetSelectedFile();
	});
}

function createLoadFileDialog() {
	$("#load_file").dialog({
		autoOpen: false,
		modal: true,
		title: "Select the file to upload",
		dialogClass: "no-close",
		maxWidth: 1000,
        width: 400,
	});
}

function listenForResetSelectedFile() {
	$('#cancel_file_upload').on("click", function() {
		$("#template_file").val("");
		$("#load_file").dialog("destroy");
	});
}

function listenForFileUpload() {
	$('#load_file_ok_button').on("click", function() {
		var file = $("#template_file").val();
		
		if(file === "") {
			alert("No file selected");
		}
		else {
			return parseFileAndStartSystem(file);
		}
	});
}

function parseFileAndStartSystem(file) {
	var initialState = parseFile(file);
	$("#load_file").dialog("destroy");
	doFirstRequest(initialState);
	
	return false;
}

function doFirstRequest(initialState) {
	if(initialState != null) {
		$.post("start", {INITIAL:initialState}, function(data) {
			window.location = data;
		});
	}
}

function parseFile(file) {
	//TODO
}