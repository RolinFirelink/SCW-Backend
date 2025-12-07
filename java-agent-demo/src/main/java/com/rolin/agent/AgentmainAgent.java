package com.rolin.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Agentmain方式的Agent入口
 * 在运行时通过Attach API加载到已运行的JVM中
 * 
 * 使用方式：
 * 1. 使用jps查看Java进程ID
 * 2. 使用Attach API加载Agent
 */
public class AgentmainAgent {
    
    /**
     * 运行时加载Agent时调用此方法
     * @param agentArgs Agent参数
     * @param inst Instrumentation实例
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("========================================");
        System.out.println("🔄 AgentmainAgent 运行时加载成功！");
        System.out.println("📝 Agent参数: " + (agentArgs != null ? agentArgs : "无"));
        System.out.println("📊 已加载类数量: " + inst.getAllLoadedClasses().length);
        System.out.println("========================================");
        
        // 添加转换器，并设置为可重新转换
        inst.addTransformer(new com.rolin.agent.bytebuddy.RuntimeTransformer(), true);
        
        // 重新转换已加载的类
        try {
            Class<?>[] classes = inst.getAllLoadedClasses();
            int count = 0;
            for (Class<?> clazz : classes) {
                // 只转换我们自己的测试类
                if (clazz.getName().startsWith("com.rolin.orangesmart.demo")) {
                    try {
                        inst.retransformClasses(clazz);
                        count++;
                        System.out.println("✅ 重新转换类: " + clazz.getName());
                    } catch (UnmodifiableClassException e) {
                        System.out.println("⚠️  无法重新转换类: " + clazz.getName() + " - " + e.getMessage());
                    }
                }
            }
            System.out.println("📊 共重新转换了 " + count + " 个类");
        } catch (Exception e) {
            System.err.println("❌ 重新转换类时出错: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("✅ 运行时Agent安装完成！");
        System.out.println("========================================");
    }
}

