#!/bin/bash
# Linux/Mac Shell脚本 - 使用Java Agent启动应用
# 使用方法: ./start-with-agent.sh [javassist|asm|bytebuddy]

AGENT_JAR="target/java-agent-demo-1.0.0.jar"
APP_JAR="../target/orange_smart-1.0.0.jar"
AGENT_TYPE="${1:-bytebuddy}"

echo "========================================"
echo "启动应用，使用 $AGENT_TYPE 实现的Agent"
echo "========================================"
echo

if [ ! -f "$AGENT_JAR" ]; then
    echo "错误: Agent JAR文件不存在: $AGENT_JAR"
    echo "请先运行: mvn clean package"
    exit 1
fi

if [ ! -f "$APP_JAR" ]; then
    echo "错误: 应用JAR文件不存在: $APP_JAR"
    echo "请先编译主项目"
    exit 1
fi

java -javaagent:"$AGENT_JAR"="$AGENT_TYPE" -jar "$APP_JAR"

