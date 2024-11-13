#!/bin/bash

py create_onvif_request.py > request.xml
curl --silent -X POST --header 'Content-Type: text/xml; charset=utf-8' -d @request.xml 'http://192.168.1.151/onvif/device_service' > response.xml

