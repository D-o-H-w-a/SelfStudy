@echo off
set JAVA_HOME=%~dp0\custom-jre
set PATH=%JAVA_HOME%\bin;%PATH%
java -jar original-Honeyz_streaming_alert-1.0-SNAPSHOT.jar
pause