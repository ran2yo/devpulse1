package com.example.devpulse1.agent;

public class MethodProfiler {
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public static void start(String methodName) {
        startTime.set(System.nanoTime());
        System.out.println("[DevPulse] " + methodName + "- start");
    }
    public static void end(String methodName) {
        long duration = System.nanoTime() - startTime.get();
        System.out.printf("[DevPulse] %s - 실행 시간: %.2f ms%n", methodName, duration / 1_000_000.0);
    }

}
