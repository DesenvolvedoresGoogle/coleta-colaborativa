function initialize() {
	var mapOptions = {
		center: new google.maps.LatLng(-34.397, 150.644),
	        zoom: 8,
	      	mapTypeId: google.maps.MapTypeId.ROADMAP
	}

	 var map = new google.maps.Map(document.getElementById("mapa_coleta"),
            mapOptions);

	// Tenta recuperar a geolocalização utilizando HTML 5.
	if (navigator.geolocation) {}

}

google.maps.event.addDomListener(window, 'load', initialize);

