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
call "%~dp0\SignAPI\Start-Inside.bat"
exit

:java404
title 去世咯~ && echo. && echo. 玩毛，Java 都没有按照说明放好……  ( 哭哭 喵 ~
echo. && echo. 按任意键开始尝试下载 MS Build of OpenJDK && echo. 记得，下载好之后把文件放对位置 && echo. 如果因为网络问题导致下载的文件损坏了，可以重新运行一次……
pause > nul
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.8-windows-x64.zip -A luanyaolingwu -o Microsoft-JDK-17.0.8-Win_x64.zip
echo. && echo. 如果WInRAR提示 "不可预料的压缩文件末端" && echo. 要不就是网有问题 要不就是微软的链接挂了 && echo.
"%~dp0\WinRAR\WinRAR.exe" Microsoft-JDK-17.0.8-Win_x64.zip
del Microsoft-JDK-17.0.8-Win_x64.zip
pause > nul