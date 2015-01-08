if(loaded === undefined) {
	var loaded = [];
}

var f = function() {
	$.get("/auth", function(data) {
		console.log(data);
	})
}

loaded.push(f);