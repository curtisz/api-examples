#!/usr/bin/perl

use strict;

use POSIX qw(strftime);
use MIME::Base64 qw(encode_base64);
use Digest::SHA qw(hmac_sha256);   
use LWP::UserAgent;                

# Constants
use constant {
        SCALR_API_KEY    => "xxxxxxxxxxxxxxx",
        SCALR_SECRET_KEY => "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        API_URL          => "https://api.scalr.net/",
        API_VERSION      => "2.3.0",
        API_AUTH_VERSION => 3,
};

my $TZ = strftime("%z", localtime);
$TZ =~ s/(\d{2})(\d{2})/$1:$2/;    

# ISO8601
my $Timestamp = strftime("%Y-%m-%dT%H:%M:%S", localtime) . $TZ;

# API call parameters
my $params_ref = {
        Action => 'FarmsList',
        Version => API_VERSION,
        Timestamp => $Timestamp,
        KeyID => SCALR_API_KEY,
        AuthVersion => API_AUTH_VERSION,
};

my $string_to_sign;

$string_to_sign = $params_ref->{'Action'} . ':' . $params_ref->{'KeyID'} . ':' . $params_ref->{'Timestamp'};

$params_ref->{'Signature'} = encode_base64( hmac_sha256($string_to_sign, SCALR_SECRET_KEY) );

$params_ref->{'Signature'} =~ s/\n//g;

my $ua = LWP::UserAgent->new;

my $response = $ua->post(API_URL, $params_ref);

if ($response->is_success) {
        print $response->content;
}
else {
        print $response->as_string;
}
