package com.rolin.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

/**
 * Attach工具类
 * 用于在运行时将Agent加载到已运行的JVM进程中
 * 
 * 注意：
 * - Java 8及之前：需要tools.jar（JDK自带，JRE没有）
 * - Java 9+：tools.jar已作为jdk.attach模块，无需单独引入
 * - 运行时需要确保在JDK环境下运行，而不是JRE
 */
public class AttachUtil {
    
    /**
     * 列出所有Java进程
     */
    public static void listJavaProcesses() {
        System.out.println("========================================");
        System.out.println("📋 当前运行的Java进程：");
        System.out.println("========================================");
        
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        if (vms.isEmpty()) {
            System.out.println("未找到Java进程");
        } else {
            for (VirtualMachineDescriptor vm : vms) {
                System.out.println("PID: " + vm.id() + " - " + vm.displayName());
            }
        }
        System.out.println("========================================");
    }
    
    /**
     * 将Agent附加到指定进程
     * @param pid 进程ID
     * @param agentJarPath Agent JAR文件路径
     * @param agentArgs Agent参数
     */
    public static void attachAgent(String pid, String agentJarPath, String agentArgs) {
        try {
            System.out.println("========================================");
            System.out.println("🔄 正在附加Agent到进程: " + pid);
            System.out.println("📦 Agent路径: " + agentJarPath);
            System.out.println("📝 Agent参数: " + (agentArgs != null ? agentArgs : "无"));
            System.out.println("========================================");
            
            VirtualMachine vm = VirtualMachine.attach(pid);
            try {
                vm.loadAgent(agentJarPath, agentArgs);
                System.out.println("✅ Agent附加成功！");
            } finally {
                vm.detach();
            }
            
        } catch (Exception e) {
            System.err.println("❌ 附加Agent失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 主方法 - 用于命令行调用
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            // 如果没有参数，列出所有进程
            listJavaProcesses();
            System.out.println("\n使用方法：");
            System.out.println("  java -cp java-agent-demo.jar com.rolin.agent.AttachUtil <PID> <AgentJarPath> [AgentArgs]");
            return;
        }
        
        if (args.length < 2) {
            System.err.println("参数不足！需要至少2个参数：PID 和 AgentJarPath");
            return;
        }
        
        String pid = args[0];
        String agentJarPath = args[1];
        String agentArgs = args.length > 2 ? args[2] : null;
        
        attachAgent(pid, agentJarPath, agentArgs);
    }
}

