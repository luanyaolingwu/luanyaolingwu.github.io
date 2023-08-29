@echo off
chcp 936
cd /d %~dp0

if not exist "%~dp0\JDK\bin\java.exe" (
    goto java404
    ) else (
    goto gogogo
    )

:gogogo
title 请输入你的选择，按回车键确认 : )
:JAVA_default_pth
set INSIDE=%OS%
set JAVAbin="%~dp0\JDK\bin"
set mclpth="%~dp0\miraimcl"
:input0
echo. && echo. && echo 	( S ) 启动机器人 ( 使用 MrXiaoM 的 qsign 分支，无需另外配置 ) && echo 	( Q ) 启动机器人和 SignAPI (使用此项要自行对接) && echo 	( X ) 退出启动脚本 && echo 	( T ) 查看Java版本 && echo 	( A ) 启动控制台以使用类似 “mcl -u” 等命令 && echo 	( R ) 启动WinRAR (暂时决定留着) && echo 	( N ) 只启动 SignAPI (暂时决定留着) && echo.
set /p input=输入你的选择：
if "%input%"=="s" goto mirai 
if "%input%"=="S" goto mirai
if "%input%"=="q" goto signapi 
if "%input%"=="Q" goto signapi
if "%input%"=="x" goto exit0 
if "%input%"=="X" goto exit0
if "%input%"=="t" goto javaver 
if "%input%"=="T" goto javaver
if "%input%"=="a" goto mcll 
if "%input%"=="A" goto mcll
if "%input%"=="r" goto wrar 
if "%input%"=="R" goto wrar
if "%input%"=="n" goto sgn1 
if "%input%"=="N" goto sgn1
cls && echo. && echo 输入有误，请重试！ 
goto input0
:sgn1
echo. && echo.写的很烂，请谅解
start "Running..." "%~dp0\SignAPI\Start-Inside.bat"
goto input0
:wrar
start "" "%~dp0\WinRAR\WinRAR.exe"
cls && echo. && echo. 已尝试性启动WinRAR
goto input0
:signapi
::暂时就这样，不管了……
echo.  && echo.
echo.Mirai懒人专用Sign项目地址 && echo. https://github.com/MrXiaoM/qsign && echo.
start "Running..." "%~dp0\SignAPI\Start-Inside.bat"
:mirai
echo.
title 运行中 … …
cd /d %mclpth%
%JAVAbin%\java.exe -jar %mclpth%\mcl.jar
cd /d %~dp0
goto exit1
:exit0
echo.%date% - %time% 用户主动退出脚本 >> run.log
exit
:exit1
title Mirai 已停止运行  && echo. && echo.  Mirai 已停止运行，按任意键返回菜单
@pause >> nul
echo.%date% - %time% 启动 Mirai 时出现问题 或 用户主动关闭 Mirai >> run.log
goto input0
:javaver
cls && echo. && echo.执行 “java --version” 的结果是：
%JAVAbin%\java.exe --version
echo.
goto input0
:mcll
echo. && echo. 具体命令用法请自行查看Mirai官方 && echo.
set PATHBAK=%PATH%
set PATH=%~dp0\JDK\bin;%PATH%
start cmd /k cd "%~dp0\miraimcl\"
set PATH=%PATHBAK%
echo.按任意键返回主菜单
pause > nul
goto input0

:java404
title 去世咯~ && echo. && echo. 玩毛，Java 都没有按照说明放好……  ( 哭哭 喵 ~
echo. && echo. 按任意键开始尝试下载 MS Build of OpenJDK && echo. 记得，下载好之后把文件放对位置 && echo. 如果因为网络问题导致下载的文件损坏了，可以重新运行一次……
pause > nul
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.8-windows-x64.zip -A luanyaolingwu -o Microsoft-JDK-17.0.8-Win_x64.zip
echo. && echo. 如果WInRAR提示 "不可预料的压缩文件末端" && echo. 要不就是网有问题 要不就是微软的链接挂了 && echo.
"%~dp0\WinRAR\WinRAR.exe" Microsoft-JDK-17.0.8-Win_x64.zip
del Microsoft-JDK-17.0.8-Win_x64.zip
pause > nul