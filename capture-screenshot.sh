#!/usr/bin/env bash

response=$(curl -s --header "Content-Type: application/json" -X POST http://localhost:9443/api/1.0/captureScreenshots -d $1)
requestId=$(echo ${response} | jq -r '.requestId')


urls=$(echo ${response} | jq -r '.results[].downloadUrl')

echo "Request id is ${requestId}"
COUNTER=1
echo "${urls}" | while read downloadUrl;
do
     curl -s -X GET ${downloadUrl} > request_${requestId}_image_${COUNTER}.png
     let COUNTER=COUNTER+1
done
echo "All images have been downloaded in current folder"
