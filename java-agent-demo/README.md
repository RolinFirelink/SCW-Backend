# Java Agent 示例项目

这是一个完整的Java Agent学习示例，展示了多种实现方式和核心功能。

## 🎯 功能特性

- ✅ **Premain方式**：JVM启动时加载Agent
- ✅ **Agentmain方式**：运行时动态加载Agent
- ✅ **三种实现**：Javassist、ASM、ByteBuddy
- ✅ **方法监控**：自动监控方法执行时间
- ✅ **异常捕获**：监控方法异常情况

## 📦 快速开始

### 1. 编译Agent

```bash
cd java-agent-demo
mvn clean package
```

### 2. 使用Premain方式（推荐）

```bash
# 使用ByteBuddy（默认）
java -javaagent:target/java-agent-demo-1.0.0.jar -jar ../target/orange_smart-1.0.0.jar

# 使用Javassist
java -javaagent:target/java-agent-demo-1.0.0.jar=javassist -jar ../target/orange_smart-1.0.0.jar

# 使用ASM
java -javaagent:target/java-agent-demo-1.0.0.jar=asm -jar ../target/orange_smart-1.0.0.jar
```

### 3. 使用Agentmain方式

```bash
# 1. 先启动应用（不使用Agent）
java -jar ../target/orange_smart-1.0.0.jar

# 2. 查看进程ID
jps -l

# 3. 附加Agent
java -cp target/java-agent-demo-1.0.0.jar com.rolin.agent.AttachDemo <PID> target/java-agent-demo-1.0.0.jar
```

### 4. 测试效果

访问测试接口：

```bash
curl http://localhost:8080/api/demo/quick
curl http://localhost:8080/api/demo/slow
curl http://localhost:8080/api/demo/all
```

## 📚 详细文档

请查看 [Java Agent使用指南.md](../Java%20Agent使用指南.md) 获取完整的使用说明。

## 🔧 技术栈

- **Java 21**
- **Javassist 3.30.2-GA** - 简单易用的字节码操作
- **ASM 9.6** - 高性能字节码操作
- **ByteBuddy 1.14.12** - 现代化的字节码操作（推荐）

## 📖 学习要点

1. **Instrumentation API**：了解核心API的使用
2. **字节码操作**：学习如何修改类字节码
3. **类加载机制**：理解Agent如何介入类加载过程
4. **性能监控**：实现无侵入的性能监控

## 🎓 适用场景

- APM（应用性能监控）系统
- 链路追踪系统
- 代码热修复
- 性能分析工具
- 安全审计工具

