package com.rolin.agent;

import java.lang.instrument.Instrumentation;

/**
 * Premain方式的Agent入口
 * 在JVM启动时通过-javaagent参数加载
 * 
 * 使用方式：
 * java -javaagent:java-agent-demo.jar=param1,param2 -jar your-app.jar
 */
public class PremainAgent {
    
    /**
     * JVM启动时调用此方法
     * @param agentArgs Agent参数，通过-javaagent:xxx.jar=param传递
     * @param inst Instrumentation实例，用于添加类转换器
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("========================================");
        System.out.println("🚀 PremainAgent 启动成功！");
        System.out.println("📝 Agent参数: " + (agentArgs != null ? agentArgs : "无"));
        System.out.println("📊 已加载类数量: " + inst.getAllLoadedClasses().length);
        System.out.println("========================================");
        
        // 根据参数选择不同的实现方式
        if (agentArgs != null) {
            String[] args = agentArgs.split(",");
            for (String arg : args) {
                switch (arg.trim().toLowerCase()) {
                    case "javassist":
                        System.out.println("📌 使用 Javassist 实现");
                        inst.addTransformer(new com.rolin.agent.javassist.PerformanceTransformer());
                        break;
                    case "asm":
                        System.out.println("📌 使用 ASM 实现");
                        inst.addTransformer(new com.rolin.agent.asm.PerformanceTransformer());
                        break;
                    case "bytebuddy":
                        System.out.println("📌 使用 ByteBuddy 实现");
                        com.rolin.agent.bytebuddy.PerformanceAgent.install(inst);
                        break;
                    default:
                        System.out.println("⚠️  未知参数: " + arg + "，使用默认的 ByteBuddy 实现");
                        com.rolin.agent.bytebuddy.PerformanceAgent.install(inst);
                }
            }
        } else {
            // 默认使用ByteBuddy
            System.out.println("📌 使用默认的 ByteBuddy 实现");
            com.rolin.agent.bytebuddy.PerformanceAgent.install(inst);
        }
        
        System.out.println("✅ Agent 安装完成，开始监控方法执行时间...");
        System.out.println("========================================");
    }
}

