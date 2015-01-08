var onload = function () {
	while (loaded.length > 0) {
	    (loaded.shift())();   
	}
}