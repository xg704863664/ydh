@echo off
setlocal EnableDelayedExpansion
call mvn clean package antrun:run  -Dmaven.test.skip=true
call mvn assembly:single -Dmaven.test.skip=true
rd /s/q !cd!\jar
pause


