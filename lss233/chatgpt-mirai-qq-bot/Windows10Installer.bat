::理论上兼容Windows 1809以及以上版本~ 希望对你有帮助，爱你们哦~
::暂时留空，明天搞 at 2023年8月7日 23:55 (北京时间)
::喵~ 改好了 ，Py311 poe-api的依赖quickjs会报错，回退到py310了 （现在是北京时间 2023年8月9日 下午 5点23分）


@echo off
chcp 936 && cls
cd /d %~dp0
set BASE_DIR=%~dp0
set PYTHON_EXECUTABLE="%~dp0\python3.10\python.exe"

::鸾瑶綾舞就是智障

title 确认无误后按任意键初始化
echo. && echo.
echo.==========================================================
echo.!!                                                      !!
echo.!! 如果您是新手，没有特殊需求。只要一路回车即可安装	!!
echo.!! 如果您在执行的过程出现错误，可以重新启动此脚本	!!
echo.!! 如果您遇到问题，敬请提交 issue，或在交流群询问	!!
echo.!!
echo.==========================================================
echo.当前的安装路径为  %BASE_DIR%
echo.提示：请注意安装路径中不要有空格，否则可能会导致安装失败 (已尝试修复，但依然不建议这么做)
echo.提示：安装前先解压程序，不要在压缩包中直接运行
echo. && echo. && echo. 仔细阅读并确认无误后，请按任意键继续 … …
pause > nul

vc_redist.x64.exe /passive /log "%~dp0\msvc.log"

echo.正在初始化 Mirai...

mkdir "%BASE_DIR%\miraimcl\config\net.mamoe.mirai-api-http"
copy "%BASE_DIR%\files\mirai-http-api-settings.yml" "%BASE_DIR%\miraimcl\config\net.mamoe.mirai-api-http\setting.yml"

echo.已经完成对Mirai的初始化 && echo. && title 已经完成对Mirai的初始化


echo.开始初始化 ChatGPT-Mirai-QQ-Bot && title 开始初始化 ChatGPT-Mirai-QQ-Bot
cd "%BASE_DIR%\chatgpt"

echo.初始化 pip …
cd "%BASE_DIR%\python3.10"
 %PYTHON_EXECUTABLE% get-pip.py >> "%~dp0\pip.log"

echo.安装依赖 …
cd "%BASE_DIR%\chatgpt"

::这是你可能要修改的地方 ====++====

::输出到文件
%PYTHON_EXECUTABLE% -m pip install -i https://mirrors.aliyun.com/pypi/simple/ --extra-index-url https://pypi.org/simple/ -r requirements.txt > "%~dp0\pip.log"

::打开控制台显示
::conhost
::%PYTHON_EXECUTABLE% -m pip install -i https://mirrors.aliyun.com/pypi/simple/ --extra-index-url https://pypi.org/simple/ -r requirements.txt

::这是你可能要修改的地方 ====++====

echo. && echo.如果下载的依赖不是最新版 && echo.请修改 https://mirrors.aliyun.com/pypi/simple/ 为 https://pypi.org/simple/ && echo.然后重新执行

echo.接下来将会打开 config.cfg (ChatGPT-Mirai-QQBot 的配置文件)，请修改里面的信息。
cd "%BASE_DIR%\chatgpt"
copy config.example.cfg config.cfg
start "编辑文件" "notepad" config.cfg
cd "%BASE_DIR%"
echo. 保存好配置文件后，任意键继续……
title 检查有无报错，无报错请按任意键继续……
pause > nul

cls 
copy "%BASE_DIR%\files\mirai\scripts\启动ChatGPT.cmd" "%~dp0"
copy "%BASE_DIR%\files\mirai\scripts\run.cmd" "%~dp0\run.cmd"
ECHO.接下来请先执行	 [ 启动ChatGPT.cmd ]，启动程序。
ECHO.然后再去执行	 [ run.cmd ] 并登录机器人 QQ 就可以开始使用了 !

title 完成！
echo.按任意键关闭脚本
pause
