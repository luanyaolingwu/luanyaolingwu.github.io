@echo off
chcp 936
cd /d %~dp0

if not exist "%~dp0\JDK\bin\java.exe" (
    goto java404
    ) else (
    goto gogogo
    )

:gogogo
title ���������ѡ�񣬰��س���ȷ�� : )
:JAVA_default_pth
set INSIDE=%OS%
set JAVAbin="%~dp0\JDK\bin"
set mclpth="%~dp0\miraimcl"
call "%~dp0\SignAPI\Start-Inside.bat"
exit

:java404
title ȥ����~ && echo. && echo. ��ë��Java ��û�а���˵���źá���  ( �޿� �� ~
echo. && echo. ���������ʼ�������� MS Build of OpenJDK && echo. �ǵã����غ�֮����ļ��Ŷ�λ�� && echo. �����Ϊ�������⵼�����ص��ļ����ˣ�������������һ�Ρ���
pause > nul
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.8-windows-x64.zip -A luanyaolingwu -o Microsoft-JDK-17.0.8-Win_x64.zip
echo. && echo. ���WInRAR��ʾ "����Ԥ�ϵ�ѹ���ļ�ĩ��" && echo. Ҫ�������������� Ҫ������΢������ӹ��� && echo.
"%~dp0\WinRAR\WinRAR.exe" Microsoft-JDK-17.0.8-Win_x64.zip
del Microsoft-JDK-17.0.8-Win_x64.zip
pause > nul