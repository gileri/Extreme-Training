var f = function() {
	$.get("/index", function(data) {
		$("#motd").html(data);
	})
}

loaded.push(f);