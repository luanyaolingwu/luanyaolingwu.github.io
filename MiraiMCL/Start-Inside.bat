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
title ���������ѡ�񣬰��س���ȷ�� : )
echo. && echo.Java �汾�ǣ� && %JAVAbin%\java.exe --version && echo.
:input1
echo. && echo.����Ҫ���е� SignAPI �汾�� && echo.    Ŀǰ����ѡ�����
echo. && echo.  ( S )  QQ - 8.9.63 && echo.  ( Q )  QQ - 8.9.68 && echo.  ( X )  QQ - 8.9.71 && echo.  ( T ) QQ - 8.9.73 && echo.  ( A ) TIM - 3.5.1 && echo.  ( R )  TIM - 3.5.2 && echo.
set /p input=�������ѡ��
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
cls && echo. && echo �������������ԣ� 
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
echo.��ѡ����� %txLibVER% �汾 ��&& echo. && title SignAPI - %txLibVER% �������� �� �� Ctrl + C ֹͣ���� )

set CLASSPATH=%APP_HOME%\lib\unidbg-fetch-qsign-1.1.9.jar;%APP_HOME%\lib\unidbg-android-105.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-status-pages-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-netty-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-events-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-network-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-utils-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-io-jvm-2.3.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.8.22.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-protobuf-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.5.1.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.7.1.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.7.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.8.22.jar;%APP_HOME%\lib\kotlin-reflect-1.8.10.jar;%APP_HOME%\lib\kotlin-stdlib-1.8.22.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.8.22.jar;%APP_HOME%\lib\config-1.4.2.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.92.Final.jar;%APP_HOME%\lib\alpn-api-1.1.3.v20160715.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.92.Final.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\annotations-23.0.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.92.Final.jar;%APP_HOME%\lib\netty-handler-4.1.92.Final.jar;%APP_HOME%\lib\netty-codec-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.92.Final.jar;%APP_HOME%\lib\netty-transport-4.1.92.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.92.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.92.Final.jar;%APP_HOME%\lib\netty-common-4.1.92.Final.jar

:loop

%JAVAbin%\java.exe -classpath "%CLASSPATH%"  MainKt --basePath=./txlib/%txLibVER%

echo. && title SignAPI - %txLibVER% �����˳����� �� �� Ctrl + C ֹͣ���� )

goto loop

pause
exit

:err
title ȥ����~ && echo. && echo. �ⲻ�Ǹ���ֱ�����е��� ����  ( �޿� �� ~
pause > nul