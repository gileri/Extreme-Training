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
	var search = getUrlParameter("searchBar")
	$("#searchBar").val(search);
	
	$.getJSON('/domains').done(function(data) {
		data.forEach(function(e) {
			var dupe = $('#domain_template').clone().appendTo('#domains_section');
			dupe.removeAttr('id');
			dupe.find('.domain_title').html(e);
			dupe.find('a').prop('href', '/ha-result-screen.html?' + $.param({domain: e}));
			dupe.removeClass('hidden');
		});
	});
	$('.loading_placeholder').hide();
}
loaded.push(f);