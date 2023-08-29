@echo off
chcp 936
cd /d %~dp0
cls

if %INSIDE% EQU %OS% (
    goto gogogo
    ) else (
    goto err
    )

:gogogo

set txLibVER=0
set APP_HOME=%~dp0
title 请输入你的选择，按回车键确认 : )
echo. && echo.Java 版本是： && %JAVAbin%\java.exe --version && echo.
:input1
echo. && echo.输入要运行的 SignAPI 版本： && echo.    目前可以选择的有
echo. && echo.  ( S )  QQ - 8.9.63 && echo.  ( Q )  QQ - 8.9.68 && echo.  ( X )  QQ - 8.9.71 && echo.  ( T ) QQ - 8.9.73 && echo.  ( A ) TIM - 3.5.1 && echo.  ( R )  TIM - 3.5.2 && echo.
set /p input=输入你的选择：
if "%input%"=="s" goto 8963
if "%input%"=="S" goto 8963
if "%input%"=="q" goto 8968
if "%input%"=="Q" goto 8968
if "%input%"=="x" goto 8971
if "%input%"=="X" goto 8971
if "%input%"=="t" goto 8973
if "%input%"=="T" goto 8973
if "%input%"=="a" goto 351
if "%input%"=="A" goto 351
if "%input%"=="r" goto 352
if "%input%"=="R" goto 352
cls && echo. && echo 输入有误，请重试！ 
goto input1

:8963
set txLibVER=8.9.63
goto init

:8968
set txLibVER=8.9.68
goto init

:8971
set txLibVER=8.9.71
goto init

:8973
set txLibVER=8.9.73
goto init

:351
set txLibVER=3.5.1
goto init

:352
set txLibVER=3.5.2
goto init

:init
echo.你选择的是 %txLibVER% 版本 ！&& echo. && title SignAPI - %txLibVER% 正在运行 （ 按 Ctrl + C 停止服务 )

set CLASSPATH=%APP_HOME%\lib\unidbg-fetch-qsign-1.1.9.jar;%APP_HOME%\lib\unidbg-android-105.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-status-pages-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-netty-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-events-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-network-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-utils-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-io-jvm-2.3.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.8.22.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-protobuf-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.5.1.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.7.1.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.7.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.8.22.jar;%APP_HOME%\lib\kotlin-reflect-1.8.10.jar;%APP_HOME%\lib\kotlin-stdlib-1.8.22.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.8.22.jar;%APP_HOME%\lib\config-1.4.2.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.92.Final.jar;%APP_HOME%\lib\alpn-api-1.1.3.v20160715.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.92.Final.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\annotations-23.0.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.92.Final.jar;%APP_HOME%\lib\netty-handler-4.1.92.Final.jar;%APP_HOME%\lib\netty-codec-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-4.1.92.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.92.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.92.Final.jar;%APP_HOME%\lib\netty-common-4.1.92.Final.jar

:loop

%JAVAbin%\java.exe -classpath "%CLASSPATH%"  MainKt --basePath=./txlib/%txLibVER%

echo. && title SignAPI - %txLibVER% 崩溃退出过… （ 按 Ctrl + C 停止服务 )

goto loop

pause
exit

:err
title 去世咯~ && echo. && echo. 这不是给你直接运行的哇 ……  ( 哭哭 喵 ~
pause > nul