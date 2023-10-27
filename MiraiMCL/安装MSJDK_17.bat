@echo off
chcp 936
cd /d %~dp0
cd ..

::conhost

echo. && echo. 按任意键开始尝试下载/安装 MS Build of OpenJDK && echo. 记得，要勾选 JAVA_HOME 环境变量 && echo. 如果因为网络问题导致下载的文件损坏了，可以重新运行一次……
pause > nul

if not exist "Microsoft-JDK-17.0.9-Win_x64.msi" (
    goto downfile
    ) else (
    goto instjdk
    )
:downfile
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.9-windows-x64.msi -A luanyaolingwu -o Microsoft-JDK-17.0.9-Win_x64.msi
:instjdk
echo. && echo. 请务必勾选 Set JAVA_HOME variable 并请等待安装完成…… 
"Microsoft-JDK-17.0.9-Win_x64.msi" /qf
del Microsoft-JDK-17.0.9-Win_x64.msi
pause > nul