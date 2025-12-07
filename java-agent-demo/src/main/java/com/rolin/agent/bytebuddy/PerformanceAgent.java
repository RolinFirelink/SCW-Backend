package com.rolin.agent.bytebuddy;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * 使用ByteBuddy实现的方法执行时间监控Agent
 * 
 * 特点：
 * - 类型安全的API
 * - 无需了解字节码指令
 * - 性能接近ASM
 * - 支持注解驱动
 * - 推荐使用
 */
public class PerformanceAgent {
    
    private static final String TARGET_PACKAGE = "com.rolin.orangesmart.demo";
    
    /**
     * 安装Agent
     */
    public static void install(Instrumentation inst) {
        new AgentBuilder.Default()
            // 指定要拦截的类（目标包下的所有类）
            .type(nameStartsWith(TARGET_PACKAGE))
            // 排除一些不需要拦截的类
            .and(not(isInterface()))
            .and(not(nameStartsWith("com.rolin.orangesmart.demo.$")))
            // 定义转换器：对所有方法应用Advice
            .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                builder
                    .visit(Advice.to(TimingAdvice.class).on(ElementMatchers.any()))
            )
            // 安装到Instrumentation
            .installOn(inst);
        
        System.out.println("[ByteBuddy Agent] ✅ Agent安装完成，监控包: " + TARGET_PACKAGE);
    }
}

