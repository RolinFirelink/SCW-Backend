package com.rolin.agent;

import com.sun.tools.attach.VirtualMachine;

/**
 * 简单的Attach示例程序
 * 
 * 使用方法：
 * java -cp java-agent-demo.jar com.rolin.agent.AttachDemo <PID> <AgentJarPath> [AgentArgs]
 * 
 * 示例：
 * java -cp java-agent-demo.jar com.rolin.agent.AttachDemo 12345 java-agent-demo.jar
 */
public class AttachDemo {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("使用方法：");
            System.out.println("  java -cp java-agent-demo.jar com.rolin.agent.AttachDemo <PID> <AgentJarPath> [AgentArgs]");
            System.out.println("\n示例：");
            System.out.println("  java -cp java-agent-demo.jar com.rolin.agent.AttachDemo 12345 java-agent-demo.jar");
            System.out.println("\n查看Java进程：");
            System.out.println("  jps -l");
            return;
        }
        
        String pid = args[0];
        String agentPath = args[1];
        String agentArgs = args.length > 2 ? args[2] : null;
        
        try {
            System.out.println("========================================");
            System.out.println("🔄 正在附加Agent到进程: " + pid);
            System.out.println("📦 Agent路径: " + agentPath);
            System.out.println("📝 Agent参数: " + (agentArgs != null ? agentArgs : "无"));
            System.out.println("========================================");
            
            VirtualMachine vm = VirtualMachine.attach(pid);
            try {
                vm.loadAgent(agentPath, agentArgs);
                System.out.println("✅ Agent附加成功！");
                System.out.println("现在可以调用测试接口观察效果了。");
            } finally {
                vm.detach();
                System.out.println("✅ 已断开连接");
            }
            
        } catch (Exception e) {
            System.err.println("❌ 附加Agent失败: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\n提示：");
            System.err.println("1. 确保使用JDK而不是JRE");
            System.err.println("2. 确保进程ID正确（使用 jps -l 查看）");
            System.err.println("3. 确保Agent JAR文件路径正确");
        }
    }
}

