### 3.1 Java Agent基础

#### Q27: 什么是Java Agent？工作原理是什么？
**答案：**

**定义：**
Java Agent是一种特殊的Java程序，可以在JVM启动时或运行时动态修改字节码，实现无侵入的监控和增强。

**两种加载方式：**

**1. 启动时加载（premain）：**
```java
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent启动，参数：" + agentArgs);
        inst.addTransformer(new MyClassTransformer());
    }
}
```

启动命令：
```bash
java -javaagent:myagent.jar=param1 -jar myapp.jar
```

**2. 运行时加载（agentmain）：**
```java
public class MyAgent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("运行时加载Agent");
        inst.addTransformer(new MyClassTransformer(), true);
        // 重新转换已加载的类
        inst.retransformClasses(targetClass);
    }
}
```

**核心API - Instrumentation：**
```java
// 添加类转换器
void addTransformer(ClassFileTransformer transformer);

// 重新转换类
void retransformClasses(Class<?>... classes);

// 重新定义类
void redefineClasses(ClassDefinition... definitions);

// 获取所有已加载的类
Class[] getAllLoadedClasses();
```

**MANIFEST.MF配置：**
```
Premain-Class: com.example.MyAgent
Agent-Class: com.example.MyAgent
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Boot-Class-Path: myagent.jar
```

#### Q28: 如何使用Java Agent实现方法执行时间监控？
**答案：**

**完整示例：**

**1. Agent类：**
```java
public class PerformanceAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new PerformanceTransformer());
    }
}
```

**2. ClassFileTransformer实现：**
```java
import javassist.*;

public class PerformanceTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, 
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) {
        try {
            // 只转换特定包下的类
            if (!className.startsWith("com/example/service")) {
                return null;
            }
            
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className.replace('/', '.'));
            
            // 遍历所有方法
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                // 方法前插入代码
                method.addLocalVariable("startTime", CtClass.longType);
                method.insertBefore(
                    "startTime = System.currentTimeMillis();"
                );
                
                // 方法后插入代码
                method.insertAfter(
                    "long endTime = System.currentTimeMillis();" +
                    "System.out.println(\"" + method.getName() + 
                    " 执行时间：\" + (endTime - startTime) + \"ms\");"
                );
            }
            
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

**3. 使用ASM实现（性能更好）：**
```java
import org.objectweb.asm.*;

public class PerformanceMethodVisitor extends MethodVisitor {
    private String methodName;
    
    public PerformanceMethodVisitor(MethodVisitor mv, String methodName) {
        super(Opcodes.ASM9, mv);
        this.methodName = methodName;
    }
    
    @Override
    public void visitCode() {
        super.visitCode();
        // 在方法开始处插入：long startTime = System.currentTimeMillis();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, 
            "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(Opcodes.LSTORE, 1);
    }
    
    @Override
    public void visitInsn(int opcode) {
        // 在return指令前插入代码
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, 
                "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(Opcodes.LLOAD, 1);
            mv.visitInsn(Opcodes.LSUB);
            // 输出执行时间
            mv.visitFieldInsn(Opcodes.GETSTATIC, 
                "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitInsn(Opcodes.SWAP);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
                "java/io/PrintStream", "println", "(J)V", false);
        }
        super.visitInsn(opcode);
    }
}
```

**字节码操作库对比：**
- **Javassist**：API简单，接近Java语法，性能较低
- **ASM**：性能高，灵活，学习成本高
- **ByteBuddy**：现代化API，性能好，易用性强（推荐）

#### Q29: ByteBuddy实现Agent的优势和示例？
**答案：**

**ByteBuddy优势：**
1. 类型安全的API
2. 无需了解字节码指令
3. 性能接近ASM
4. 支持注解驱动

**示例：方法拦截**
```java
public class PerformanceAgent {
    public static void premain(String args, Instrumentation inst) {
        new AgentBuilder.Default()
            // 指定要拦截的类
            .type(ElementMatchers.nameStartsWith("com.example.service"))
            // 定义转换器
            .transform((builder, typeDescription, classLoader, module) ->
                builder.method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(TimeInterceptor.class))
            )
            .installOn(inst);
    }
}

// 拦截器
public class TimeInterceptor {
    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> zuper,
                                   @Origin Method method) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return zuper.call();
        } finally {
            long cost = System.currentTimeMillis() - start;
            System.out.println(method.getName() + " cost: " + cost + "ms");
        }
    }
}
```

**进阶：使用@Advice（性能更好）**
```java
public class PerformanceAgent {
    public static void premain(String args, Instrumentation inst) {
        new AgentBuilder.Default()
            .type(ElementMatchers.nameStartsWith("com.example.service"))
            .transform((builder, typeDescription, classLoader, module) ->
                builder.visit(Advice.to(TimingAdvice.class)
                    .on(ElementMatchers.any()))
            )
            .installOn(inst);
    }
}

public class TimingAdvice {
    @Advice.OnMethodEnter
    public static long enter() {
        return System.currentTimeMillis();
    }
    
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                           @Advice.Origin String method,
                           @Advice.Thrown Throwable throwable) {
        long cost = System.currentTimeMillis() - startTime;
        System.out.println(method + " cost: " + cost + "ms");
        if (throwable != null) {
            System.out.println("Exception: " + throwable.getMessage());
        }
    }
}
```

### 3.2 APM探针技术

#### Q30: 什么是APM？常见的APM系统有哪些？
**答案：**

**APM（Application Performance Management/Monitoring）：**
应用性能管理/监控，用于监控和管理应用程序的性能和可用性。

**核心功能：**
1. **性能监控**：响应时间、吞吐量、错误率
2. **链路追踪**：分布式调用链追踪
3. **指标采集**：CPU、内存、GC、线程等
4. **日志聚合**：集中式日志管理
5. **告警通知**：性能异常告警
6. **拓扑图**：服务依赖关系可视化

**常见APM系统：**

**1. 开源APM：**
- **SkyWalking**（Apache）：
  - 国产，中文文档友好
  - 支持Java、.NET、Node.js、PHP等
  - 无侵入，基于Java Agent
  - UI功能强大
  
- **Pinpoint**（Naver）：
  - 韩国开源，适合Java生态
  - 调用链可视化出色
  - 资源占用较高
  
- **Zipkin**（Twitter）：
  - 轻量级，易于集成
  - 需要代码侵入（Spring Cloud Sleuth）
  
- **Jaeger**（Uber）：
  - CNCF项目，云原生
  - 支持OpenTelemetry
  
- **CAT**（大众点评）：
  - 实时监控
  - 适合大规模分布式系统

**2. 商业APM：**
- **Datadog**
- **New Relic**
- **AppDynamics**
- **Dynatrace**
- **OneAPM**（国产）
- **听云**（国产）

#### Q31: SkyWalking的架构和工作原理？
**答案：**

**架构组件：**
1. **Agent**：探针，负责数据采集
2. **OAP（Observability Analysis Platform）**：后端服务，数据分析和聚合
3. **Storage**：存储（ES、H2、MySQL等）
4. **UI**：Web界面

**工作流程：**
```
应用程序 → Agent采集 → gRPC/HTTP → OAP处理 → 存储 → UI展示
```

**Agent原理：**
1. 基于Java Agent和ByteBuddy实现
2. 拦截关键方法（HTTP、RPC、DB、MQ等）
3. 采集Trace、Metrics、Logs数据
4. 通过gRPC批量发送到OAP

**链路追踪实现（基于Google Dapper）：**
```java
TraceContext {
    TraceId: "uuid",           // 全局唯一标识
    ParentSegmentId: "xxx",    // 父Segment ID
    SpanId: 1,                 // Span ID
    ParentSpanId: 0,           // 父Span ID
    StartTime: 1234567890,
    EndTime: 1234567900
}
```

**插件机制：**
- 通过SPI机制加载插件
- 每个插件针对特定框架（如Tomcat、Dubbo、MySQL）
- 插件定义拦截点和增强逻辑

**示例插件结构：**
```java
// 定义拦截点
@ClassMatch(className = "org.apache.catalina.core.StandardEngineValve")
public class TomcatInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {
    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("invoke");
                }
                public String getMethodsInterceptor() {
                    return "org.apache.skywalking.apm.plugin.tomcat.TomcatInterceptor";
                }
            }
        };
    }
}

// 拦截器
public class TomcatInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, 
                            Object[] allArguments, Class<?>[] argumentsTypes,
                            MethodInterceptResult result) {
        // 创建Span
        AbstractSpan span = ContextManager.createEntrySpan(operationName, null);
        span.setComponent(ComponentsDefine.TOMCAT);
        Tags.URL.set(span, request.getRequestURL().toString());
    }
    
    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, 
                             Object[] allArguments, Class<?>[] argumentsTypes,
                             Object ret) {
        // 停止Span
        ContextManager.stopSpan();
        return ret;
    }
}
```

**性能优化：**
- 采样率控制（默认全量采集）
- 异步发送数据
- 批量发送减少网络开销
- 对象池复用减少GC

#### Q32: 如何实现一个简单的APM探针？
**答案：**

**核心功能：**
1. 方法执行时间统计
2. HTTP接口监控
3. 数据库查询监控
4. 异常捕获

**实现步骤：**

**1. Agent入口：**
```java
public class SimpleAPMAgent {
    public static void premain(String args, Instrumentation inst) {
        // 初始化数据收集器
        MetricsCollector.init();
        
        // 安装转换器
        new AgentBuilder.Default()
            // 监控所有Controller
            .type(isAnnotatedWith(RestController.class))
            .transform(new ControllerTransformer())
            // 监控JDBC
            .type(hasSuperType(named("java.sql.Statement")))
            .transform(new JdbcTransformer())
            .installOn(inst);
        
        // 启动数据上报线程
        ReporterThread.start();
    }
}
```

**2. Controller监控：**
```java
public class ControllerAdvice {
    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String method,
                            @Advice.AllArguments Object[] args) {
        TraceContext context = TraceContext.create();
        context.setMethod(method);
        context.setStartTime(System.currentTimeMillis());
        ThreadLocalContext.set(context);
        return context.getStartTime();
    }
    
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                           @Advice.Origin String method,
                           @Advice.Return Object result,
                           @Advice.Thrown Throwable throwable) {
        long cost = System.currentTimeMillis() - startTime;
        TraceContext context = ThreadLocalContext.get();
        context.setCost(cost);
        
        if (throwable != null) {
            context.setException(throwable);
        }
        
        // 收集指标
        MetricsCollector.collect(context);
        ThreadLocalContext.remove();
    }
}
```

**3. 数据收集器：**
```java
public class MetricsCollector {
    private static BlockingQueue<TraceContext> queue = 
        new LinkedBlockingQueue<>(10000);
    
    public static void collect(TraceContext context) {
        queue.offer(context);
    }
    
    public static List<TraceContext> getAndClear() {
        List<TraceContext> list = new ArrayList<>();
        queue.drainTo(list);
        return list;
    }
}
```

**4. 数据上报：**
```java
public class ReporterThread extends Thread {
    private static final String SERVER_URL = "http://apm-server:8080/api/traces";
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);  // 每10秒上报一次
                
                List<TraceContext> traces = MetricsCollector.getAndClear();
                if (traces.isEmpty()) {
                    continue;
                }
                
                // 转换为JSON并上报
                String json = JSON.toJSONString(traces);
                HttpUtil.post(SERVER_URL, json);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

**5. TraceContext数据结构：**
```java
public class TraceContext {
    private String traceId;        // 链路ID
    private String spanId;         // Span ID
    private String parentSpanId;   // 父Span ID
    private String method;         // 方法名
    private long startTime;        // 开始时间
    private long cost;             // 耗时
    private String serviceName;    // 服务名
    private String ip;             // IP地址
    private Throwable exception;   // 异常信息
    
    public static TraceContext create() {
        TraceContext context = new TraceContext();
        context.traceId = UUID.randomUUID().toString();
        context.spanId = IdGenerator.generate();
        context.serviceName = System.getProperty("service.name");
        context.ip = IpUtil.getLocalIp();
        return context;
    }
}
```

### 3.3 eBPF技术

#### Q33: 什么是eBPF？在APM中的应用？
**答案：**

**eBPF（extended Berkeley Packet Filter）：**
- Linux内核技术，允许在内核空间运行沙箱程序
- 无需修改内核代码或加载内核模块
- 提供高性能、低开销的监控能力

**特点：**
1. **无侵入**：不需要修改应用代码
2. **高性能**：内核级监控，开销极低
3. **安全**：沙箱机制，验证器保证安全
4. **实时**：内核级事件捕获

**在APM中的应用：**

**1. 网络监控：**
- TCP连接跟踪
- HTTP请求监控
- 网络延迟分析

**2. 系统调用跟踪：**
- 文件I/O监控
- 系统调用耗时统计

**3. 性能分析：**
- CPU火焰图
- 内存分配跟踪
- 函数调用跟踪

**eBPF vs Java Agent：**
| 特性 | eBPF | Java Agent |
|------|------|------------|
| 侵入性 | 无侵入 | 字节码增强 |
| 性能开销 | 极低（<1%） | 较低（3-5%） |
| 语言支持 | 所有语言 | 仅Java |
| 实现难度 | 高 | 中等 |
| 功能范围 | 系统级 | 应用级 |
| 业务信息 | 有限 | 丰富 |

**典型eBPF APM产品：**
- **Pixie**（CNCF项目）：Kubernetes APM
- **Cilium**：网络监控和安全
- **Falco**：运行时安全检测

**eBPF示例（跟踪TCP连接）：**
```c
// BPF程序（C语言）
#include <linux/bpf.h>
#include <linux/ptrace.h>

BPF_HASH(start, u32, u64);

int trace_tcp_connect(struct pt_regs *ctx) {
    u32 pid = bpf_get_current_pid_tgid();
    u64 ts = bpf_ktime_get_ns();
    start.update(&pid, &ts);
    return 0;
}

int trace_tcp_connect_ret(struct pt_regs *ctx) {
    u32 pid = bpf_get_current_pid_tgid();
    u64 *tsp = start.lookup(&pid);
    if (tsp != 0) {
        u64 delta = bpf_ktime_get_ns() - *tsp;
        bpf_trace_printk("TCP connect latency: %lld ns\\n", delta);
        start.delete(&pid);
    }
    return 0;
}
```

**Python加载eBPF程序：**
```python
from bcc import BPF

# 加载BPF程序
b = BPF(src_file="tcp_trace.c")
b.attach_kprobe(event="tcp_v4_connect", fn_name="trace_tcp_connect")
b.attach_kretprobe(event="tcp_v4_connect", fn_name="trace_tcp_connect_ret")

# 读取输出
b.trace_print()
```

**Java应用中使用eBPF：**
- 通过JNI调用eBPF程序
- 或使用现成的eBPF APM产品
- 与Java Agent配合使用：eBPF做系统级监控，Agent做应用级监控
