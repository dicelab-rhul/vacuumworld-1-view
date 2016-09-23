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
		if($("#template_file").val() === "") {
			alert("No file selected");
		}
		else {
			$("#load_file").dialog("destroy");
			return false;
		}
	});
}