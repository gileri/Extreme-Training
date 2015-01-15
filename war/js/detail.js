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
	var key = getUrlParameter("key");
	$.getJSON("/training", {
		key : key
	}).done(function(data) {
		$("#training_title").html(data.title);
		$("#training_description").html(data.description);
		
		data.exercises.forEach(function(e) {
			var ex = $("#ex_template").clone().appendTo("#ex_section");
			ex.removeAttr('id');

			ex.find(".ex_title").html(e.title);
			ex.find(".ex_desc").html(e.description);
			ex.find(".ex_duration").html(Math.round(e.duration/60) + " min");
			ex.removeClass("hidden");
		});
		$('#loading_placeholder').hide();
	});
}
loaded.push(f);