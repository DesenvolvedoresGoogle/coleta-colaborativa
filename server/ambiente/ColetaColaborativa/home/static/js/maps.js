$(document).ready( function() {
	var pontoInicial = new google.maps.LatLng(-21.76249, -43.352834);
	var pontoColeta;
	var mapa;

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

});

