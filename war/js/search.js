function getUrlParameter(sParam) {
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) {
			return sParameterName[1];
		}
	}
}

var f = function() {
	var search = getUrlParameter("searchBar");
	var domain = getUrlParameter("domain");
	if(domain) {
		
	}
	$("#searchBar").val(search);
	$.post("/search", {
		searchBar : search
	}, function(data) {
		var obj = $.parseJSON(data);

		var numTrainings = 0;
		obj.trainings.forEach(function(e) {
			var duration = 0;
			e.exercises.forEach(function(f) {
				duration = duration + parseInt(f.duration);
			});
			var dupe = $("#training_template").clone().appendTo(
					"#training_section");

			dupe.find(".name").html(e.name);
			dupe.find(".duration").prepend(Math.round(duration / 60) + " min");
			dupe.find("a")
					.prop("href", "/ha-training-detail.html?key=" + e.key);
			dupe.removeClass("hidden");
		});
		obj.exercises.forEach(function(e) {
			var dupe = $("#exercise_template").clone().appendTo(
					"#exercise_section");

			dupe.find(".name").html(e.name);
			dupe.find(".duration").html(Math.round(e.duration/60) + " min");
			dupe.find("a")
					.prop("href", "/ha-training-detail.html?key=" + e.keytraining);
			dupe.removeClass("hidden");
		});
		obj.news.forEach(function(e) {
			$('#news_section').append($("<p></p>").html(e));
		});
		$('.loading_placeholder').hide();
	})
}
loaded.push(f);