@echo off
REM Windows批处理脚本 - 使用Java Agent启动应用
REM 使用方法: start-with-agent.bat [javassist|asm|bytebuddy]

set AGENT_JAR=target\java-agent-demo-1.0.0.jar
set APP_JAR=..\target\orange_smart-1.0.0.jar
set AGENT_TYPE=%1

if "%AGENT_TYPE%"=="" set AGENT_TYPE=bytebuddy

echo ========================================
echo 启动应用，使用 %AGENT_TYPE% 实现的Agent
echo ========================================
echo.

if not exist %AGENT_JAR% (
    echo 错误: Agent JAR文件不存在: %AGENT_JAR%
    echo 请先运行: mvn clean package
    pause
    exit /b 1
)

if not exist %APP_JAR% (
    echo 错误: 应用JAR文件不存在: %APP_JAR%
    echo 请先编译主项目
    pause
    exit /b 1
)

java -javaagent:%AGENT_JAR%=%AGENT_TYPE% -jar %APP_JAR%

pause

