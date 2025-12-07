package com.rolin.agent.bytebuddy;

import net.bytebuddy.asm.Advice;

/**
 * 运行时Agent的Advice类
 */
public class RuntimeTimingAdvice {
    
    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String method) {
        long startTime = System.currentTimeMillis();
        System.out.println("[Runtime Agent] 🔄 进入方法: " + method);
        return startTime;
    }
    
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                          @Advice.Origin String method,
                          @Advice.Thrown Throwable throwable) {
        long cost = System.currentTimeMillis() - startTime;
        
        if (throwable != null) {
            System.out.println("[Runtime Agent] ❌ 方法 " + method + 
                             " 执行异常，耗时: " + cost + "ms");
        } else {
            System.out.println("[Runtime Agent] ✅ 方法 " + method + 
                             " 执行完成，耗时: " + cost + "ms");
        }
    }
}

