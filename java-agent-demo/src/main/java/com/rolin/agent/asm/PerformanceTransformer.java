package com.rolin.agent.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 使用ASM实现的方法执行时间监控转换器
 * 
 * 特点：
 * - 性能高，接近原生性能
 * - 灵活，可以精确控制字节码
 * - 学习成本较高，需要了解字节码指令
 */
public class PerformanceTransformer implements ClassFileTransformer {
    
    private static final String TARGET_PACKAGE = "com/rolin/orangesmart/demo";
    
    @Override
    public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
        
        if (!className.startsWith(TARGET_PACKAGE)) {
            return null;
        }
        
        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            PerformanceClassVisitor cv = new PerformanceClassVisitor(Opcodes.ASM9, cw, className);
            cr.accept(cv, ClassReader.EXPAND_FRAMES);
            
            byte[] result = cw.toByteArray();
            if (cv.isModified()) {
                System.out.println("[ASM Agent] ✅ 已增强类: " + className.replace('/', '.'));
            }
            return result;
            
        } catch (Exception e) {
            System.err.println("[ASM Agent] ❌ 转换类 " + className + " 时出错: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 类访问器，用于访问类的结构
     */
    static class PerformanceClassVisitor extends ClassVisitor {
        private String className;
        private boolean modified = false;
        
        public PerformanceClassVisitor(int api, ClassVisitor cv, String className) {
            super(api, cv);
            this.className = className;
        }
        
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor,
                                       String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
            
            // 跳过构造函数和静态初始化块
            if (name.equals("<init>") || name.equals("<clinit>")) {
                return mv;
            }
            
            // 创建方法访问器
            return new PerformanceMethodVisitor(api, mv, className, name);
        }
        
        public boolean isModified() {
            return modified;
        }
    }
    
    /**
     * 方法访问器，用于在方法中插入监控代码
     */
    static class PerformanceMethodVisitor extends MethodVisitor {
        private String className;
        private String methodName;
        private int localVarIndex;
        
        public PerformanceMethodVisitor(int api, MethodVisitor mv, String className, String methodName) {
            super(api, mv);
            this.className = className;
            this.methodName = methodName;
        }
        
        @Override
        public void visitCode() {
            super.visitCode();
            
            // 在方法开始处插入代码：
            // long startTime = System.currentTimeMillis();
            // System.out.println("[ASM Agent] 进入方法: ...");
            
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", 
                             "currentTimeMillis", "()J", false);
            localVarIndex = 2; // 局部变量索引（0=this, 1=第一个参数，2=startTime）
            mv.visitVarInsn(Opcodes.LSTORE, localVarIndex);
            
            // 输出进入方法日志
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", 
                            "Ljava/io/PrintStream;");
            mv.visitLdcInsn("[ASM Agent] 进入方法: " + className.replace('/', '.') + "." + methodName + "()");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", 
                            "println", "(Ljava/lang/String;)V", false);
        }
        
        @Override
        public void visitInsn(int opcode) {
            // 在return指令前插入代码计算执行时间
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                // long endTime = System.currentTimeMillis();
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", 
                                 "currentTimeMillis", "()J", false);
                mv.visitVarInsn(Opcodes.LSTORE, localVarIndex + 1);
                
                // long cost = endTime - startTime;
                mv.visitVarInsn(Opcodes.LLOAD, localVarIndex + 1);
                mv.visitVarInsn(Opcodes.LLOAD, localVarIndex);
                mv.visitInsn(Opcodes.LSUB);
                mv.visitVarInsn(Opcodes.LSTORE, localVarIndex + 2);
                
                // System.out.println("[ASM Agent] 方法 ... 执行完成，耗时: " + cost + "ms");
                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", 
                                "Ljava/io/PrintStream;");
                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", 
                                "<init>", "()V", false);
                mv.visitLdcInsn("[ASM Agent] 方法 " + methodName + "() 执行完成，耗时: ");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", 
                                "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitVarInsn(Opcodes.LLOAD, localVarIndex + 2);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", 
                                "append", "(J)Ljava/lang/StringBuilder;", false);
                mv.visitLdcInsn("ms");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", 
                                "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", 
                                "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", 
                                "println", "(Ljava/lang/String;)V", false);
            }
            
            super.visitInsn(opcode);
        }
    }
}

