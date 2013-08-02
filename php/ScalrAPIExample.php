<?php

define('SCALR_API_KEY', 'xxxxxxxxxxxxxxx');
define('SCALR_SECRET_KEY', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx');
define('API_URL', 'https://api.scalr.net/');
define('API_VERSION', '2.3.0');
define('API_AUTH_VERSION', '3');
define('TIMEZONE', 'UTC');

date_default_timezone_set(TIMEZONE);

// Build query arguments list
$params = array(
    'Action'     => 'FarmsList',
    'Version'    => API_VERSION,
    'Timestamp'  => date("Y-m-d H:i:s"),
    'KeyID'      => SCALR_API_KEY,
    'AuthVersion'=> API_AUTH_VERSION
);

// Generate string for sign
$string_to_sign = $params['Action'] . ":" . $params['KeyID'] . ":" . $params['Timestamp'];
$params['Signature'] = base64_encode(hash_hmac('SHA256', $string_to_sign, SCALR_SECRET_KEY, 1));

//GET request
// Build query
$query = http_build_query($params);

// Execute query
$c = file_get_contents(API_URL."?{$query}");
print_r($c);
