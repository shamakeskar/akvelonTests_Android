@ECHO off

echo "Start build LinkedIn_Android_Tests.apk:"
cd ..
call ant debug
cd bin
move /y LinkedIn_Android_Tests-debug.apk LinkedIn_Android_Tests.apk
cd ../Runner
echo "'LinkedIn_Android_Tests.apk' successfully created in 'bin/' folder."

SET args=
:loop
IF NOT "%1"=="" (
   SET args=%args% %1
   SHIFT
   GOTO loop
)

java -jar runner.jar%args%