#!/usr/bin/env bash

requestId=$1
curl -s -X GET http://localhost:9443/api/1.0/fetchRequestDetail/${requestId} | jq '.'
