$(document).ready(function() {
	$(function() {
		if(session.getAttribute("GRID") != null) {
			populateGridAndRefresh(session.getAttribute("GRID"));
		}
	});
});

function populateGridAndRefresh(size, imagesPathsArray) {
	var j=1;
	var i=1;
	
	var grid="";
	
	for(var path in imagesPathsArray) {
		if(j > size) {
			j=1;
			i++;
		}
		
		var id = "image_" + i + "_" + j;
		grid += "<img src=\"" + path + "\" class=\"grid_image\" id=\"" + id + "\" style=\"border: 1px solid #000000;\" />"; 
	}
	
	$("#grid").append(grid);
	
	doAjaxRequestForNewUpdate();
}

function doAjaxRequestForNewUpdate() {
	$.post("grid", {REQUEST_CODE:"GET_STATE"}, function(data) {
		var images = data.images;
		var imgs = [];
		
		for(var img in images) {
			imgs.append(img);
		}
		
		populateGridAndRefresh(data.size, imgs);
	});
}