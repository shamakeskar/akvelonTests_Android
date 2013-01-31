#!/bin/bash

if [ -z "$1" ];then
    APK="../Build/LinkedIn.apk"
else
    APK=$(cd "$1"; pwd)
fi
if [ -z "$2" ];then
    KEYSTORE="$HOME/.android/debug.keystore"
else
    KEYSTORE=$(cd "$2"; pwd)
fi

if [ ! -f "$APK" ];then
    echo "'$APK' not specified or not exist. Please check parameters: first - path to APK, second - path to keystore file."
    exit 1
fi
if [ ! -f "$KEYSTORE" ];then
    echo "'$KEYSTORE' not specified or not exist. Please check parameters: first - path to APK, second - path to keystore file."
    exit 1
fi
jarsigner -digestalg SHA1 -sigalg MD5withRSA -keystore "$KEYSTORE" "$APK" androiddebugkey