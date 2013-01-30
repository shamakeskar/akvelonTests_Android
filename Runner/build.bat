echo "Start build LinkedIn_Android_Tests.apk:"
cd ..
call ant debug
cd bin
move /y LinkedIn_Android_Tests-debug.apk LinkedIn_Android_Tests.apk
cd ../Runner
echo "'LinkedIn_Android_Tests.apk' successfully created in 'bin/' folder."
pause