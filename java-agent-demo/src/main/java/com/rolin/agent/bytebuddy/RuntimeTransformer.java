package com.rolin.agent.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 运行时转换器
 * 用于agentmain方式，在运行时重新转换已加载的类
 * 
 * 注意：这个实现使用ByteBuddy直接操作字节码
 */
public class RuntimeTransformer implements ClassFileTransformer {
    
    private static final String TARGET_PACKAGE = "com/rolin/orangesmart/demo";
    
    @Override
    public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
        
        if (!className.startsWith(TARGET_PACKAGE)) {
            return null;
        }
        
        // 如果类没有被重新定义（首次加载），跳过（只处理retransform）
        if (classBeingRedefined == null) {
            return null;
        }
        
        try {
            String targetClassName = className.replace('/', '.');
            
            // 使用ByteBuddy重新定义类
            byte[] transformed = new ByteBuddy()
                .redefine(classBeingRedefined)
                .visit(Advice.to(RuntimeTimingAdvice.class).on(ElementMatchers.any()))
                .make()
                .getBytes();
            
            System.out.println("[Runtime Agent] ✅ 转换类: " + targetClassName);
            return transformed;
                
        } catch (Exception e) {
            System.err.println("[Runtime Agent] ❌ 转换类 " + className + " 时出错: " + e.getMessage());
            // 某些类可能无法转换（如final类、接口等），这是正常的
        }
        
        return null;
    }
}

