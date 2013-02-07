#!/bin/bash

echo "Start build LinkedIn_Android_Tests.apk:"
cd ..
ant debug
cd bin
mv LinkedIn_Android_Tests-debug.apk LinkedIn_Android_Tests.apk
cd ../Runner
echo "'LinkedIn_Android_Tests.apk' successfully created in 'bin/' folder."

args=""
while [ -n "$1" ]; do
	args="$args$1"
	shift
done

java -jar runner.jar $args