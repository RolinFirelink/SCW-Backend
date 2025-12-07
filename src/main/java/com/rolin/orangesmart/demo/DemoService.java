package com.rolin.orangesmart.demo;

import org.springframework.stereotype.Service;

/**
 * 用于演示Java Agent效果的测试服务类
 * 
 * 这个类的方法会被Agent拦截并添加执行时间监控
 */
@Service
public class DemoService {
    
    /**
     * 快速方法 - 用于测试短时间执行的方法
     */
    public String quickMethod() {
        return "快速方法执行完成";
    }
    
    /**
     * 慢速方法 - 模拟耗时操作
     */
    public String slowMethod() throws InterruptedException {
        Thread.sleep(100); // 模拟耗时100ms
        return "慢速方法执行完成，耗时约100ms";
    }
    
    /**
     * 带参数的方法
     */
    public String methodWithParams(String name, int age) {
        return String.format("姓名: %s, 年龄: %d", name, age);
    }
    
    /**
     * 可能抛出异常的方法
     */
    public String methodWithException(boolean shouldThrow) {
        if (shouldThrow) {
            throw new RuntimeException("这是一个测试异常");
        }
        return "方法正常执行";
    }
    
    /**
     * 递归方法 - 用于测试复杂场景
     */
    public int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

