package com.example.devpulse1.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.instrument.ClassFileTransformer;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ClassTransformer implements ClassFileTransformer {


    private static List<String> includePrefixes = new ArrayList<>();
    private static List<String> excludePrefixes = new ArrayList<>();


    public static void setIncludePrefixes(String raw) {
        includePrefixes = Arrays.stream(raw.split(","))
                .map(s -> s.replace('.', '/').trim())
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public static void setExcludePrefixes(String raw) {
        excludePrefixes = Arrays.stream(raw.split(","))
                .map(s -> s.replace('.', '/').trim())
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private boolean shouldTransform(String className) {
        if (!includePrefixes.isEmpty()) {
            boolean matched = includePrefixes.stream().anyMatch(className::startsWith);
            if (!matched) return false;
        }

        if (!excludePrefixes.isEmpty()) {
            boolean matched = excludePrefixes.stream().anyMatch(className::startsWith);
            if (matched) return false;
        }

        return true;
    }

    @Override
    public byte[] transform(Module module, ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {

        if (!shouldTransform(className)) {
            return null;
        }

        System.out.println("[DevPulseTransformer] Hooking: " + className);

        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {

                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    return new AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
                        @Override
                        protected void onMethodEnter() {
                            visitLdcInsn(className + "." + name);
                            invokeStatic(Type.getType(MethodProfiler.class),
                                    new Method("start", "(Ljava/lang/String;)V"));
                        }

                        @Override
                        protected void onMethodExit(int opcode) {
                            visitLdcInsn(className + "." + name);
                            invokeStatic(Type.getType(MethodProfiler.class),
                                    new Method("end", "(Ljava/lang/String;)V"));
                        }
                    };
                }
            };

            cr.accept(cv, 0);
            return cw.toByteArray();

        } catch (Exception e) {
            System.err.println("[DevPulse] Transform error: " + e.getMessage());
            return null;
        }
    }
}