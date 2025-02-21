@echo off
set JAVA_HOME=%~dp0\custom-jre
set PATH=%JAVA_HOME%\bin;%PATH%
java -jar Honeyz_streaming_alert.jar
pause