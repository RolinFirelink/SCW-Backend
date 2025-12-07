package com.rolin.orangesmart.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于演示Java Agent效果的测试Controller
 * 
 * 提供REST接口来触发测试方法，观察Agent的监控效果
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    
    @Autowired
    private DemoService demoService;
    
    /**
     * 测试快速方法
     */
    @GetMapping("/quick")
    public String testQuick() {
        return demoService.quickMethod();
    }
    
    /**
     * 测试慢速方法
     */
    @GetMapping("/slow")
    public String testSlow() throws InterruptedException {
        return demoService.slowMethod();
    }
    
    /**
     * 测试带参数的方法
     */
    @GetMapping("/params")
    public String testParams(@RequestParam(defaultValue = "张三") String name,
                            @RequestParam(defaultValue = "25") int age) {
        return demoService.methodWithParams(name, age);
    }
    
    /**
     * 测试异常方法
     */
    @GetMapping("/exception")
    public String testException(@RequestParam(defaultValue = "false") boolean throwException) {
        try {
            return demoService.methodWithException(throwException);
        } catch (Exception e) {
            return "捕获到异常: " + e.getMessage();
        }
    }
    
    /**
     * 测试递归方法（计算斐波那契数列）
     */
    @GetMapping("/fibonacci")
    public String testFibonacci(@RequestParam(defaultValue = "10") int n) {
        int result = demoService.fibonacci(n);
        return String.format("斐波那契数列第%d项: %d", n, result);
    }
    
    /**
     * 综合测试 - 调用多个方法
     */
    @GetMapping("/all")
    public String testAll() throws InterruptedException {
        StringBuilder result = new StringBuilder();
        result.append("=== 综合测试开始 ===\n");
        
        result.append("1. ").append(demoService.quickMethod()).append("\n");
        result.append("2. ").append(demoService.slowMethod()).append("\n");
        result.append("3. ").append(demoService.methodWithParams("测试", 30)).append("\n");
        result.append("4. 斐波那契(10) = ").append(demoService.fibonacci(10)).append("\n");
        
        result.append("=== 综合测试完成 ===");
        return result.toString();
    }
}

