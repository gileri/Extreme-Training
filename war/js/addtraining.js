var training_exercises = [];

var recalculate_duration = function() {
	var duration = 0;
	training_exercises.forEach(function(t) {
		duration += t.duration;
	});
	$('#totalTimeValue').html(Math.round(duration/60) + ' min');
	return duration;
}

var f = function() {
	$("#training_domain").select2();
	
	recalculate_duration();

	$.getJSON('/domains').done(function(data) {
		data.forEach(function(e) {
			var option = $('<option></option>').val(e).html(e);
			$('#training_domain').append(option);
		});
	});

	$('#exercise_section').on('click', '.exercise_delete', function(e) {
		var id_ex = $(this).attr('data-id_ex');
		$('.exercise[data-id_ex=' + id_ex + ']').remove();
		recalculate_duration();
	});

	$('#add_exercise').on(
			'click',
			function(e) {
				var title = $('#new_ex_title').val();
				var description = $('#new_ex_desc').val();

				var hours = parseInt($('#new_ex_hours').val());
				var minutes = parseInt($('#new_ex_minutes').val());
				var seconds = parseInt($('#new_ex_seconds').val());
				var duration = hours * 3600 + minutes * 60 + seconds;

				var exercise = $('#exercise_template').clone();
				exercise.removeAttr('id');
				exercise.attr('data-id_ex', training_exercises.length)
				exercise.find('.exercise_id').text(
						training_exercises.length + 1);
				exercise.find('.exercise_title').text(title);
				exercise.find('.exercise_desc').text(description);
				exercise.find('.exercise_time').text(Math.round(duration/60) + ' min');
				exercise.find('.exercise_delete').attr('data-id_ex',
						training_exercises.length);
				exercise.removeClass("hidden");

				$('#exercise_section').append(exercise);

				training_exercises.push({
					title : title,
					description : description,
					duration : duration
				});
				recalculate_duration();
			});

	$('#upload_training').on('click', function(e) {
		var training = {
			title : $('#training_title').val(),
			description : $('#training_desc').val(),
			domain : $('#training_domain').val(),
			exercises : training_exercises
		};
		$.ajax('/addtraining', {
			type: 'POST',
			data: JSON.stringify(training),
		    contentType: 'application/json; charset=utf-8',
		}).done(function(data) {
			console.log(data);
		}).fail(function(res) {
			console.log(res);
		});
		;
	});
}
loaded.push(f);