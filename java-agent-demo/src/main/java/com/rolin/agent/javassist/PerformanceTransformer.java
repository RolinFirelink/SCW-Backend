package com.rolin.agent.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 使用Javassist实现的方法执行时间监控转换器
 * 
 * 特点：
 * - API简单，接近Java语法
 * - 性能较低，适合开发阶段
 * - 易于理解和维护
 */
public class PerformanceTransformer implements ClassFileTransformer {
    
    // 要监控的包路径
    private static final String TARGET_PACKAGE = "com/rolin/orangesmart/demo";
    
    @Override
    public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
        
        // 只转换目标包下的类
        if (!className.startsWith(TARGET_PACKAGE)) {
            return null;
        }
        
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className.replace('/', '.'));
            
            // 如果类已经被冻结，则解冻
            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }
            
            boolean modified = false;
            
            // 遍历所有方法
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                // 跳过构造函数和静态初始化块
                if (method.getName().equals("<init>") || method.getName().equals("<clinit>")) {
                    continue;
                }
                
                // 添加局部变量用于记录开始时间
                method.addLocalVariable("__startTime__", CtClass.longType);
                
                // 在方法开始处插入代码
                method.insertBefore(
                    "__startTime__ = System.currentTimeMillis();" +
                    "System.out.println(\"[Javassist Agent] 进入方法: " + className.replace('/', '.') + "." + 
                    method.getName() + "()\");"
                );
                
                // 在方法返回前插入代码
                method.insertAfter(
                    "long __endTime__ = System.currentTimeMillis();" +
                    "long __cost__ = __endTime__ - __startTime__;" +
                    "System.out.println(\"[Javassist Agent] 方法 " + method.getName() + 
                    "() 执行完成，耗时: \" + __cost__ + \"ms\");",
                    true  // 即使方法抛出异常也会执行
                );
                
                modified = true;
            }
            
            if (modified) {
                System.out.println("[Javassist Agent] ✅ 已增强类: " + className.replace('/', '.'));
                return ctClass.toBytecode();
            }
            
        } catch (Exception e) {
            System.err.println("[Javassist Agent] ❌ 转换类 " + className + " 时出错: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}

