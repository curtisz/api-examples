#coding:utf-8
import datetime
import urllib
import base64
import hmac
import hashlib


SCALR_API_KEY = 'xxxxxxxxxxxxxxx'
SCALR_SECRET_KEY = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'

API_URL = 'https://api.scalr.net/'
API_VERSION = '2.3.0'
API_AUTH_VERSION =  '3'

API_ACTION = "FarmsList"


def main(key_id, secret_key):
    timestamp = datetime.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S")

    params = {
        "Action": API_ACTION,
        "Version": API_VERSION,
        "AuthVersion": API_AUTH_VERSION,
        "Timestamp": timestamp,
        "KeyID": key_id,
        "Signature":  base64.b64encode(hmac.new(secret_key, ":".join([API_ACTION, key_id, timestamp]), hashlib.sha256).digest()),
    }

    urlparams = urllib.urlencode(params)
    req = urllib.urlopen(API_URL, urlparams)

    return req.read()


if __name__ == "__main__":
    print main(SCALR_API_KEY, SCALR_SECRET_KEY)
