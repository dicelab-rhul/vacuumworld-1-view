var stopped = false;

$(document).ready(function() {
	$(function() {
		while($("#grid").html() === "") {
			continue;
		}
		listenForStopButton();
		refresh();
	});
});

function listenForStopButton() {
	$("#stop_button").on("click", function() {
		$.post("grid", {REQUEST_CODE:"STOP_FORWARD"}, function(data) {
			if(data === "stopped") {
				stopped = true;
				window.location = "index.jsp";
			}
		});
	});
}

function refresh() {
	doAjaxRequestForNewUpdate();
}

function doAjaxRequestForNewUpdate() {
	$.post("grid", {REQUEST_CODE:"GET_STATE"}, function(data) {
		var state = JSON.parse(JSON.stringify(data));
		var images = state.images;
		var imgs = [];
		
		for(var i = 0; i < images.length; i++) {
		    imgs.push(images[i].image);
		}
		
		populateGridAndRefresh(state.size, imgs);
	});
}

function populateGridAndRefresh(size, images) {
	var counter = 0;
	
	$("#grid").html("");
	$("#grid").append("<br />");
	
	for(var i=0; i<images.length; i++) {
		if(counter >= size) {
			counter = 0;
			var br = $("<br />");
			br.appendTo("#grid");
		}
		
		counter++;
		var img = $("<img>");
		img.attr("src", images[i]);
		img.attr("class", "grid_image");
		img.appendTo("#grid");
	}
	
	if(!stopped) {
		doAjaxRequestForNewUpdate();
	}
}