
var crypto = require('crypto');
var https = require('https');
var qs = require('querystring');

var SCALR_API_KEY = 'aaaaaaa';
var SCALR_SECRET_KEY = 'bbbbbbbbbbbbbbbbbbbbbbbbbbb';

var API_URL = 'https://api.scalr.net/';
var API_VERSION = '2.3.0';
var API_AUTH_VERSION = '3';

var API_ACTION = 'FarmsList';

var TIMESTAMP = new Date().toISOString();

var params = {
	Action: API_ACTION,
	Version: API_VERSION,
	AuthVersion: API_AUTH_VERSION,
	Timestamp: TIMESTAMP,
	KeyID: SCALR_API_KEY,
	Signature: crypto.createHmac('sha256', SCALR_SECRET_KEY).update([API_ACTION, SCALR_API_KEY, TIMESTAMP].join(':')).digest('base64')
};

// make request
var req = https.get(API_URL + '?' + qs.stringify(params), function(res) {
	console.log('status code: ', res.statusCode);
	console.log('headers: ', res.headers);

	// response handler
	var data = [];
	res.on('data', function(chunk) {
		data.push(chunk);
	});
	res.on('end', function() {
		console.log('response: ', Buffer.concat(data).toString());
	});
}).on('error', function(err) {
	console.log('error making request: ' + err);
});
