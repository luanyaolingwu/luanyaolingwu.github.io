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
:input0
echo. && echo. && echo 	( S ) ���������� ( ʹ�� MrXiaoM �� qsign ��֧�������������� ) && echo 	( Q ) ���������˺� SignAPI (ʹ�ô���Ҫ���жԽ�) && echo 	( X ) �˳������ű� && echo 	( T ) �鿴Java�汾 && echo 	( A ) ��������̨��ʹ������ ��mcl -u�� ������ && echo 	( R ) ����WinRAR (��ʱ��������) && echo 	( N ) ֻ���� SignAPI (��ʱ��������) && echo.
set /p input=�������ѡ��
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
cls && echo. && echo �������������ԣ� 
goto input0
:sgn1
echo. && echo.д�ĺ��ã����½�
start "Running..." "%~dp0\SignAPI\Start-Inside.bat"
goto input0
:wrar
start "" "%~dp0\WinRAR\WinRAR.exe"
cls && echo. && echo. �ѳ���������WinRAR
goto input0
:signapi
::��ʱ�������������ˡ���
echo.  && echo.
echo.Mirai����ר��Sign��Ŀ��ַ && echo. https://github.com/MrXiaoM/qsign && echo.
start "Running..." "%~dp0\SignAPI\Start-Inside.bat"
:mirai
echo.
title ������ �� ��
cd /d %mclpth%
%JAVAbin%\java.exe -jar %mclpth%\mcl.jar
cd /d %~dp0
goto exit1
:exit0
echo.%date% - %time% �û������˳��ű� >> run.log
exit
:exit1
title Mirai ��ֹͣ����  && echo. && echo.  Mirai ��ֹͣ���У�����������ز˵�
@pause >> nul
echo.%date% - %time% ���� Mirai ʱ�������� �� �û������ر� Mirai >> run.log
goto input0
:javaver
cls && echo. && echo.ִ�� ��java --version�� �Ľ���ǣ�
%JAVAbin%\java.exe --version
echo.
goto input0
:mcll
echo. && echo. ���������÷������в鿴Mirai�ٷ� && echo.
set PATHBAK=%PATH%
set PATH=%~dp0\JDK\bin;%PATH%
start cmd /k cd "%~dp0\miraimcl\"
set PATH=%PATHBAK%
echo.��������������˵�
pause > nul
goto input0

:java404
title ȥ����~ && echo. && echo. ��ë��Java ��û�а���˵���źá���  ( �޿� �� ~
echo. && echo. ���������ʼ�������� MS Build of OpenJDK && echo. �ǵã����غ�֮����ļ��Ŷ�λ�� && echo. �����Ϊ�������⵼�����ص��ļ����ˣ�������������һ�Ρ���
pause > nul
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.8-windows-x64.zip -A luanyaolingwu -o Microsoft-JDK-17.0.8-Win_x64.zip
echo. && echo. ���WInRAR��ʾ "����Ԥ�ϵ�ѹ���ļ�ĩ��" && echo. Ҫ�������������� Ҫ������΢������ӹ��� && echo.
"%~dp0\WinRAR\WinRAR.exe" Microsoft-JDK-17.0.8-Win_x64.zip
del Microsoft-JDK-17.0.8-Win_x64.zip
pause > nul