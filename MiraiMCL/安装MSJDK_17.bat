@echo off
chcp 936
cd /d %~dp0
cd ..

::conhost

echo. && echo. ���������ʼ��������/��װ MS Build of OpenJDK && echo. �ǵã�Ҫ��ѡ JAVA_HOME �������� && echo. �����Ϊ�������⵼�����ص��ļ����ˣ�������������һ�Ρ���
pause > nul

if not exist "Microsoft-JDK-17.0.9-Win_x64.msi" (
    goto downfile
    ) else (
    goto instjdk
    )
:downfile
curl -L https://aka.ms/download-jdk/microsoft-jdk-17.0.9-windows-x64.msi -A luanyaolingwu -o Microsoft-JDK-17.0.9-Win_x64.msi
:instjdk
echo. && echo. ����ع�ѡ Set JAVA_HOME variable ����ȴ���װ��ɡ��� 
"Microsoft-JDK-17.0.9-Win_x64.msi" /qf
del Microsoft-JDK-17.0.9-Win_x64.msi
pause > nul