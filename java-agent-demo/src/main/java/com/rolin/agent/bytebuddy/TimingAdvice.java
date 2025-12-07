package com.rolin.agent.bytebuddy;

import net.bytebuddy.asm.Advice;

/**
 * ByteBuddy的Advice类
 * 使用@Advice注解来定义方法进入和退出时的逻辑
 * 
 * 这种方式性能最好，因为ByteBuddy会在编译时优化
 */
public class TimingAdvice {
    
    /**
     * 方法进入时调用
     * @return 返回开始时间，会在exit方法中通过@Advice.Enter获取
     */
    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String method) {
        long startTime = System.currentTimeMillis();
        System.out.println("[ByteBuddy Agent] 进入方法: " + method);
        return startTime;
    }
    
    /**
     * 方法退出时调用（包括正常返回和异常）
     * @param startTime 通过@Advice.Enter获取的进入时间
     * @param method 方法签名
     * @param throwable 如果有异常，这里会包含异常对象
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                          @Advice.Origin String method,
                          @Advice.Thrown Throwable throwable) {
        long cost = System.currentTimeMillis() - startTime;
        
        if (throwable != null) {
            System.out.println("[ByteBuddy Agent] 方法 " + method + 
                             " 执行异常，耗时: " + cost + "ms，异常: " + throwable.getClass().getSimpleName());
        } else {
            System.out.println("[ByteBuddy Agent] 方法 " + method + 
                             " 执行完成，耗时: " + cost + "ms");
        }
    }
}

