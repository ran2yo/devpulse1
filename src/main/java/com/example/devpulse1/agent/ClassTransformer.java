package com.example.devpulse1.agent;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[]   transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        //변형 대상 클래스 필터링(ex 특정 패키지만 감시)
        if(className.startsWith("testapp/SampleApp")){
            System.out.println("[DevPulseTransFomer ] 감시 대상 클래스: " + className);
            return  null;
            //ASN 등으로 바이트 코드 조작 가능

        }

        System.out.println("[DevPulseTransformer ] Hooking" + className);

        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

                    //run 메서드만 타겟
                    if (name.equals("run") && descriptor.equals("()V")) {
                        return new MethodVisitor(Opcodes.ASM9, mv) {

                            public void visitInsn() {
                                mv.visitCode();
                                //MethodProfile.start("run");
                                mv.visitLdcInsn("run");
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "devpulse/agent/MethodProfiler",
                                        "start", "(Ljava/lang/String:)V",
                                        false);
                            }
                            public void visitIsns(int opcode) {
                                //리턴 전에 MethodProfiler.end("run");
                             if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                                 mv.visitLdcInsn("run");
                                 mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                                         "devpulse/agent/MethodProfiler",
                                         "end", "(Ljava/lang/String;)V", false);
                             }
                             mv.visitInsn(opcode);
                            }
                        };
                    }
                    return mv;
                }
            };
            cr.accept(cv, 0);
            return cw.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
