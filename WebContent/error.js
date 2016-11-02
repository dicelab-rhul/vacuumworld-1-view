function showError(text) {
	var errorDiv = createErrorDiv(text);
	var errorDialog = createDialogFromErrorDiv(errorDiv);
	
	errorDialog.dialog("open");
	listenToErrorDialogOkButton();
}

function listenToErrorDialogOkButton() {
	$("#error_ok_button").on("click", manageClickOnErrorOkButton);
}

function manageClickOnErrorOkButton() {
	$("#error_dialog").dialog("destroy");
}

function createErrorDiv(text) {
	var errorDiv = $("<div id=\"error_dialog\" class=\"dialog\" style=\"text-align: center;margin: auto;\">");
	
	errorDiv.append("<form action=\"\" method=\"post\"></form>");
	errorDiv.append("<div class=\"centered_div\">");
	errorDiv.append("<p id=\"error_message\">" + text + "</p>");
	errorDiv.append("<input type=\"submit\" value=\"Ok\" id=\"error_ok_button\">");
	errorDiv.append("</div>");
	errorDiv.append("</form>");
	errorDiv.append("</div>");
	
	return errorDiv;
}

function createDialogFromErrorDiv(errorDiv) {
	return errorDiv.dialog({
		autoOpen: false,
		modal: true,
		dialogClass: "no-close",
		title: "Error"
	});
}