@ECHO off

IF [%1]==[] (
    SET apk=..\Build\LinkedIn.apk
) ELSE (
    SET apk="%~f1"
)
IF [%2]==[] (
    SET keystore=%USERPROFILE%\.android\debug.keystore
) ELSE (
    SET keystore="%~f2"
)

IF NOT EXIST %apk% (
    ECHO APK not specified or not exist. Please check parameters: first - path to APK, second - path to keystore file.
    PAUSE
    EXIT /B 1
)
IF NOT EXIST %keystore% (
    ECHO Keystore not specified or not exist. Please check parameters: first - path to APk, second - path to keystore file.
    PAUSE
    EXIT /B 1
)

call jarsigner.exe -digestalg SHA1 -sigalg MD5withRSA -keystore %keystore% %apk% androiddebugkey

IF %errorlevel%==0 (
    ECHO 
    ECHO %apk% successfully resigned!
) ELSE (
    ECHO 
    ECHO %apk% has not been resigned!
)

PAUSE