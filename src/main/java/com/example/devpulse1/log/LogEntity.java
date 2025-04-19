package com.example.devpulse1.log;

public class LogEntity {
    public long timestamp;
    public String threadName;
    public String methodName;
    public double elapsedTimeMs;
    public int depth;

    public String trace;

    public LogEntity(String methodName, double elapsedTimeMs, int depth, String trace) {
        this.timestamp = System.currentTimeMillis();
        this.threadName = Thread.currentThread().getName();
        this.methodName = methodName;
        this.elapsedTimeMs = elapsedTimeMs;
        this.depth = depth;
        this.trace = trace;

    }
}
