#!/bin/bash

if [ -z "$HOCKEY_APP_DEVELOP_TOKEN" ]; then
	printf "FAILED!"
	printf "ERROR: Please set environment variable HOCKEY_APP_DEVELOP_TOKEN for HOCKEY_APP_DEVELOP_TOKEN."
	exit 1
fi

if [ -z "$CI_PIPELINE_ID" ]; then
	printf "FAILED!"
	printf "ERROR: Please set environment variable CI_PIPELINE_ID for AWS CI_PIPELINE_ID."
	exit 1
fi

if [ -z "$CI_PROJECT_NAME" ]; then
	printf "FAILED!"
	printf "ERROR: Please set environment variable CI_PROJECT_NAME for CI_PROJECT_NAME."
	exit 1
fi

if [ -z "$DEVELOP_MESSAGE" ]; then
	printf "FAILED!"
	printf "ERROR: Please set environment variable DEVELOP_MESSAGE for DEVELOP_MESSAGE."
	exit 1
fi

printf "Uploading ${CI_PROJECT_NAME}-v${CI_PIPELINE_ID}-debug.apk to HockeyApp.. "

JSON=$( curl \
-F "status=2" \
-F "notify=1" \
-F "notes=${DEVELOP_MESSAGE}" \
-F "notes_type=0" \
-F "ipa=@${CI_PROJECT_NAME}-v${CI_PIPELINE_ID}-debug.apk" \
-H "X-HockeyAppToken: ${HOCKEY_APP_DEVELOP_TOKEN}" \
https://rink.hockeyapp.net/api/2/apps/upload )

URL=$( printf ${JSON} | sed 's/\\\//\//g' | sed -n 's/.*"public_url"\s*:\s*"\([^"]*\)".*/\1/p' )
if [ -z "$URL" ]; then
	printf "FAILED!"
	printf "Build uploaded, but no reply from server. Please contact support@hockeyapp.net"
	exit 1
fi

printf "OK!"
printf "Build was successfully uploaded to HockeyApp and is available at:"
printf ${URL}

