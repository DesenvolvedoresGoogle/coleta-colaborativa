$(document).ready( function() {
	var pontoInicial = new google.maps.LatLng(-21.76249, -43.352834);
	var pontoColeta;
	var mapa;

	document.getElementById('lat').value = pontoInicial.lat();
	document.getElementById('long').value = pontoInicial.lng();

	var mapOptions = {
		zoom: 17,
		center: pontoInicial
	};

  	mapa = new google.maps.Map(document.getElementById('mapa_coleta'),
          	mapOptions);


  	pontoColeta = new google.maps.Marker({
    		map: mapa,
		draggable: true,
	   	animation: google.maps.Animation.DROP,
    		position: pontoInicial
  	});

  	google.maps.event.addListener(pontoColeta, 'click', function(event) {
  		if (pontoColeta.getAnimation() != null) {
			pontoColeta.setAnimation(null);
  		} else {
    			pontoColeta.setAnimation(google.maps.Animation.BOUNCE);
  		}
	});
  	google.maps.event.addListener(pontoColeta, 'dragend', function(event) {
		document.getElementById('lat').value = event.latLng.lat();
		document.getElementById('long').value = event.latLng.lng();
  	});

});

