var loaded = function() {
	$.get("/index", function(data) {
		$("#motd").html(data);
	})
	
}