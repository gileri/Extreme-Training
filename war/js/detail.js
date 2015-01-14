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
//	current = new Date()
//	time = date.getTime();
//	$(".countdown").flipcountdown({
//		speedFlip : 60,
//		tick : function() {
//			currentTime = time - 1000;
//			time = currentTime;
//			date = new Date(currentTime);
//			return date;
//		}
//	});
	var key = getUrlParameter("key");
	$.getJSON("/training", {
		key : key
	}).done(function(data) {
		data.exercises.forEach(function(e) {
			var ex = $("#ex_template").clone().appendTo("#ex_section");

			ex.find("#ex_title").html(e.title);
			ex.find("#ex_desc").html(e.description);
			ex.find("#ex_duration").html(e.duration);
			ex.removeClass("hidden");
		});
		$('#loading_placeholder').hide();
	});
}
loaded.push(f);