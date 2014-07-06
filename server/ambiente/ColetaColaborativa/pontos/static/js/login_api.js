function signinCallback(authResult) {
	if (authResult['access_token']) {
		loadProfile();
		// Successfully authorized
		document.getElementById('signinButton').setAttribute('style', 'display: none');
	} else if (authResult['error']) {
		// There was an error.
//		document.getElementById('form_ponto').setAttribute('style', 'display: none');
		// Possible error codes:
		//   "access_denied" - User denied access to your app
		//   "immediate_failed" - Could not automatially log in the user
		// console.log('There was an error: ' + authResult['error']);
	}
}

function loadProfile() {
	gapi.client.load('plus', 'v1', function() {
		var request = gapi.client.plus.people.get({
			'userId': 'me'
		});

		request.execute(function(response) {
			alert(response['displayName'] + '\n' + response['id'] + '\n' + response['url']);
		});
	});
}
