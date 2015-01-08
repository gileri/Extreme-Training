function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}

var f = function() {
	var search =getUrlParameter("searchBar");
	$("#searchBar").val(search);
	$.post("/search", {searchBar: search}, function(data) {
		var obj = $.parseJSON(data);
		
		var numTrainings = 0;
		obj.trainings.forEach(function(e) {
			var duration = 0;
			e.forEach(function(f) {
				duration = duration + f.duration;
			});
			var dupe = $("#training_template")
			.clone()
			.appendTo("#training_section");
			
			dupe.find(".name").html(e.name);
			dupe.find(".duration").html(duration);
			dupe.removeClass("hidden");
		});
		
		obj.exercise.forEach(function(e) {
			var dupe = $("#exercise_template")
			.clone()
			.appendTo("#exercise_section");
			
			dupe.find(".name").html(e.name);
			dupe.find(".duration").html(e.duration);
			dupe.removeClass("hidden");
		});
	})
}
loaded.push(f);