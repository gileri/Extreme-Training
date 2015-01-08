if (loaded === undefined) {
	var loaded = [];
}

var f = function() {
	$.getJSON("/auth", function(data) {
		if(data.logout !== undefined) {
			$('<a>', {
				text : "Logout",
				title : "Logout",
				href : data.logout
			}).addClass("btn btn-danger btn-sm").appendTo("#loginbar");
		} else {
			data.forEach(function(e) {
				var button = $("<a></a>");
				button.text = e.name;
				$('<a>', {
					text : e.name,
					title : e.name,
					href : e.loginURL
				}).addClass("btn btn-primary btn-sm").appendTo("#loginbar");
			});
		}
	})
}

loaded.push(f);