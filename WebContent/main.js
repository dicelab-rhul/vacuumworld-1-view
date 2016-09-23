function validateForm(formName, subElementName) {
    var x = document.forms[formName][subElementName].value;
    
    if (x == null || x == "") {
        return false;
    }
    
    return true;
}

function positiveInteger(formName, subElementName) {
	var n = document.forms[formName][subElementName].value;
	var x = Number(n);
	
	return $.isNumeric(x) && x > 0;
}