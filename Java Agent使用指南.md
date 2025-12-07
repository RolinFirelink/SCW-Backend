# Java Agent 使用指南

本指南将帮助你了解如何使用本项目中的Java Agent示例，并观察它们的效果。

## 📚 目录

1. [项目结构](#项目结构)
2. [编译Agent](#编译agent)
3. [Premain方式（启动时加载）](#premain方式启动时加载)
4. [Agentmain方式（运行时加载）](#agentmain方式运行时加载)
5. [测试效果](#测试效果)
6. [三种实现方式对比](#三种实现方式对比)

---

## 📁 项目结构

```
java-agent-demo/
├── pom.xml                                    # Agent模块的Maven配置
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/rolin/agent/
│   │   │       ├── PremainAgent.java         # Premain方式入口
│   │   │       ├── AgentmainAgent.java       # Agentmain方式入口
│   │   │       ├── AttachUtil.java           # Attach工具类
│   │   │       ├── javassist/                # Javassist实现
│   │   │       ├── asm/                      # ASM实现
│   │   │       └── bytebuddy/                # ByteBuddy实现
│   │   └── resources/
│   │       └── META-INF/
│   │           └── MANIFEST.MF               # Agent清单文件
└── README.md

src/main/java/com/rolin/orangesmart/demo/
├── DemoService.java                           # 测试服务类
└── DemoController.java                       # 测试Controller
```

---

## 🔨 编译Agent

### 步骤1：进入Agent模块目录

```bash
cd java-agent-demo
```

### 步骤2：使用Maven编译并打包

```bash
# 如果系统有Maven
mvn clean package

# 或者使用项目自带的Maven Wrapper（Windows）
..\mvnw.cmd clean package

# 或者使用项目自带的Maven Wrapper（Linux/Mac）
../mvnw clean package
```

编译成功后，会在 `target` 目录下生成 `java-agent-demo-1.0.0.jar` 文件。

### 步骤3：使用快速启动脚本（可选）

我们提供了快速启动脚本，方便使用：

**Windows:**
```bash
# 使用ByteBuddy（默认）
start-with-agent.bat

# 使用Javassist
start-with-agent.bat javassist

# 使用ASM
start-with-agent.bat asm
```

**Linux/Mac:**
```bash
# 添加执行权限
chmod +x start-with-agent.sh

# 使用ByteBuddy（默认）
./start-with-agent.sh

# 使用Javassist
./start-with-agent.sh javassist

# 使用ASM
./start-with-agent.sh asm
```

---

## 🚀 Premain方式（启动时加载）

Premain方式是在JVM启动时通过 `-javaagent` 参数加载Agent。这是最常用的方式。

### 方式1：使用ByteBuddy实现（推荐）

```bash
java -javaagent:java-agent-demo.jar=bytebuddy -jar target/orange_smart-1.0.0.jar
```

或者不指定参数（默认使用ByteBuddy）：

```bash
java -javaagent:java-agent-demo.jar -jar target/orange_smart-1.0.0.jar
```

### 方式2：使用Javassist实现

```bash
java -javaagent:java-agent-demo.jar=javassist -jar target/orange_smart-1.0.0.jar
```

### 方式3：使用ASM实现

```bash
java -javaagent:java-agent-demo.jar=asm -jar target/orange_smart-1.0.0.jar
```

### 观察效果

启动后，你会看到类似以下的输出：

```
========================================
🚀 PremainAgent 启动成功！
📝 Agent参数: bytebuddy
📊 已加载类数量: 1234
========================================
📌 使用 ByteBuddy 实现
[ByteBuddy Agent] ✅ Agent安装完成，监控包: com.rolin.orangesmart.demo
✅ Agent 安装完成，开始监控方法执行时间...
========================================
```

然后当你调用测试接口时，会看到方法执行时间的监控信息：

```
[ByteBuddy Agent] 进入方法: com.rolin.orangesmart.demo.DemoService.quickMethod()
[ByteBuddy Agent] 方法 quickMethod() 执行完成，耗时: 2ms
```

---

## 🔄 Agentmain方式（运行时加载）

Agentmain方式可以在应用运行时动态加载Agent，无需重启应用。

### 步骤1：启动应用（不使用Agent）

```bash
java -jar target/orange_smart-1.0.0.jar
```

### 步骤2：查看Java进程ID

打开新的终端窗口，使用以下命令查看Java进程：

```bash
# Windows
jps -l

# Linux/Mac
jps -l
```

你会看到类似输出：

```
12345 com.rolin.orangesmart.OrangeSmartApplication
```

记下进程ID（例如：12345）。

### 步骤3：使用Attach工具加载Agent

#### 方式1：使用AttachUtil工具类

```bash
# 先编译AttachUtil（需要tools.jar，JDK自带）
java -cp java-agent-demo.jar com.rolin.agent.AttachUtil <PID> <AgentJarPath> [AgentArgs]

# 示例
java -cp java-agent-demo.jar com.rolin.agent.AttachUtil 12345 java-agent-demo.jar
```

#### 方式2：编写简单的Attach程序

创建一个 `AttachDemo.java` 文件：

```java
import com.sun.tools.attach.VirtualMachine;

public class AttachDemo {
    public static void main(String[] args) throws Exception {
        String pid = args[0]; // 进程ID
        String agentPath = args[1]; // Agent JAR路径
        
        VirtualMachine vm = VirtualMachine.attach(pid);
        try {
            vm.loadAgent(agentPath);
            System.out.println("Agent加载成功！");
        } finally {
            vm.detach();
        }
    }
}
```

编译并运行：

```bash
javac -cp java-agent-demo.jar AttachDemo.java
java -cp .:java-agent-demo.jar AttachDemo 12345 java-agent-demo.jar
```

### 观察效果

加载Agent后，在应用的控制台会看到：

```
========================================
🔄 AgentmainAgent 运行时加载成功！
📝 Agent参数: null
📊 已加载类数量: 5678
========================================
✅ 重新转换类: com.rolin.orangesmart.demo.DemoService
✅ 重新转换类: com.rolin.orangesmart.demo.DemoController
📊 共重新转换了 2 个类
✅ 运行时Agent安装完成！
========================================
```

之后调用接口时，会看到监控信息：

```
[Runtime Agent] 🔄 进入方法: com.rolin.orangesmart.demo.DemoService.quickMethod()
[Runtime Agent] ✅ 方法 quickMethod() 执行完成，耗时: 1ms
```

---

## 🧪 测试效果

### 启动应用后，访问以下测试接口：

#### 1. 测试快速方法
```bash
curl http://localhost:8080/api/demo/quick
```

**预期输出（控制台）：**
```
[ByteBuddy Agent] 进入方法: com.rolin.orangesmart.demo.DemoService.quickMethod()
[ByteBuddy Agent] 方法 quickMethod() 执行完成，耗时: 1ms
```

#### 2. 测试慢速方法
```bash
curl http://localhost:8080/api/demo/slow
```

**预期输出（控制台）：**
```
[ByteBuddy Agent] 进入方法: com.rolin.orangesmart.demo.DemoService.slowMethod()
[ByteBuddy Agent] 方法 slowMethod() 执行完成，耗时: 101ms
```

#### 3. 测试带参数的方法
```bash
curl "http://localhost:8080/api/demo/params?name=李四&age=30"
```

#### 4. 测试异常方法
```bash
curl "http://localhost:8080/api/demo/exception?throwException=true"
```

**预期输出（控制台）：**
```
[ByteBuddy Agent] 进入方法: com.rolin.orangesmart.demo.DemoService.methodWithException(boolean)
[ByteBuddy Agent] 方法 methodWithException(boolean) 执行异常，耗时: 0ms，异常: RuntimeException
```

#### 5. 测试递归方法
```bash
curl "http://localhost:8080/api/demo/fibonacci?n=10"
```

#### 6. 综合测试
```bash
curl http://localhost:8080/api/demo/all
```

---

## 📊 三种实现方式对比

| 特性 | Javassist | ASM | ByteBuddy |
|------|-----------|-----|-----------|
| **API易用性** | ⭐⭐⭐⭐⭐ 最简单 | ⭐⭐ 需要了解字节码 | ⭐⭐⭐⭐ 类型安全 |
| **性能** | ⭐⭐ 较低 | ⭐⭐⭐⭐⭐ 最高 | ⭐⭐⭐⭐ 接近ASM |
| **学习成本** | ⭐ 最低 | ⭐⭐⭐⭐⭐ 最高 | ⭐⭐ 中等 |
| **功能灵活性** | ⭐⭐⭐ 中等 | ⭐⭐⭐⭐⭐ 最灵活 | ⭐⭐⭐⭐ 很灵活 |
| **推荐场景** | 开发阶段、简单需求 | 性能要求极高 | 生产环境（推荐） |

### 性能测试建议

你可以通过调用 `/api/demo/fibonacci?n=30` 来测试不同实现的性能差异：

```bash
# 使用Javassist
java -javaagent:java-agent-demo.jar=javassist -jar target/orange_smart-1.0.0.jar

# 使用ASM
java -javaagent:java-agent-demo.jar=asm -jar target/orange_smart-1.0.0.jar

# 使用ByteBuddy
java -javaagent:java-agent-demo.jar=bytebuddy -jar target/orange_smart-1.0.0.jar
```

---

## 🔍 核心方法说明

### Instrumentation API

Java Agent的核心是 `Instrumentation` 接口，它提供了以下关键方法：

1. **`addTransformer(ClassFileTransformer transformer)`**
   - 添加类转换器
   - 在类加载时会被调用，可以修改类的字节码

2. **`retransformClasses(Class<?>... classes)`**
   - 重新转换已加载的类
   - 用于运行时加载Agent的场景

3. **`redefineClasses(ClassDefinition... definitions)`**
   - 重新定义类
   - 功能更强大，但限制更多

4. **`getAllLoadedClasses()`**
   - 获取所有已加载的类
   - 用于查看当前JVM中加载的类

### Premain vs Agentmain

| 特性 | Premain | Agentmain |
|------|---------|-----------|
| **加载时机** | JVM启动时 | 运行时 |
| **入口方法** | `premain(String, Instrumentation)` | `agentmain(String, Instrumentation)` |
| **使用场景** | 应用启动时就需要监控 | 动态添加监控，无需重启 |
| **实现难度** | 简单 | 需要处理已加载的类 |

---

## ⚠️ 注意事项

1. **MANIFEST.MF配置**
   - `Premain-Class`: 指定premain入口类
   - `Agent-Class`: 指定agentmain入口类
   - `Can-Retransform-Classes: true`: 允许重新转换类（agentmain需要）

2. **类加载器**
   - Agent的类加载器是系统类加载器
   - 确保Agent依赖的库在classpath中

3. **性能影响**
   - Agent会拦截所有匹配的类和方法
   - 建议只监控必要的包，避免影响性能

4. **工具依赖**
   - Attach功能需要 `tools.jar`（JDK自带，JRE没有）
   - 确保使用JDK而不是JRE

---

## 📖 扩展学习

1. **SkyWalking Agent**: 学习生产级APM Agent的实现
2. **Arthas**: 学习如何使用Agent进行线上诊断
3. **ByteBuddy文档**: https://bytebuddy.net/
4. **ASM文档**: https://asm.ow2.io/
5. **Javassist文档**: https://www.javassist.org/

---

## 🐛 常见问题

### Q1: 启动时提示找不到Premain-Class
**A:** 检查MANIFEST.MF文件是否正确配置，确保打包时包含了MANIFEST.MF。

### Q2: Attach时提示找不到tools.jar
**A:** 确保使用JDK而不是JRE，tools.jar在JDK的lib目录下。

### Q3: Agent没有生效
**A:** 
- 检查类名匹配规则是否正确
- 确认目标类在Agent加载后才被加载（premain方式）
- 查看控制台是否有错误信息

### Q4: 如何只监控特定方法？
**A:** 在Transformer中添加方法匹配逻辑，例如：
```java
// ByteBuddy示例
.method(named("slowMethod").or(named("quickMethod")))
```

---

## 🎉 总结

通过本示例，你应该已经了解了：

1. ✅ Java Agent的两种加载方式（premain和agentmain）
2. ✅ 三种字节码操作库的使用（Javassist、ASM、ByteBuddy）
3. ✅ 如何实现方法执行时间监控
4. ✅ 如何在实际项目中应用Java Agent

现在你可以尝试修改代码，实现自己的监控逻辑！

