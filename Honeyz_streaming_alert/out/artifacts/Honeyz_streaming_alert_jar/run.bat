@echo off
set JAVAFX_SDK=%~dp0javafx
java --module-path %JAVAFX_SDK%\lib --add-modules javafx.controls -jar %~dp0Honeyz_streaming_alert.jar
exit