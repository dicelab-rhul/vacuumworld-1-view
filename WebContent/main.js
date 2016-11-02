function validateForm(x) { 
	if (x === null || x === "") {    	
        return false;
    }
    
    return true;
}

function positiveInteger(n) {	
	return n.match(/^([1-9]|10)$/) != null;
}