if(loaded === undefined) {
	var loaded = [];
}

var f = function() {
	$.get("/index", function(data) {
		$("#motd").html(data);
	})
}

loaded.push(f);