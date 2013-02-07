#!/bin/bash

#
# Script for run single emulator and prepeare to run tests
#

# Kill device
adb emu kill 2>&1 >/dev/null || true
sleep 5

# Start emulator and wait complete loading.
( emulator -avd "$1" ) &
while [[ $(adb shell getprop init.svc.bootanim 2>&1 >/dev/null) != "" ]]; do
    sleep 5
done
sleep 5

# Unblock device.
adb shell input keyevent 82
adb shell input keyevent 4  

# Remove folder with screenshots.
adb shell rm -Rf /sdcard/0_tmp
adb shell ls /sdcard