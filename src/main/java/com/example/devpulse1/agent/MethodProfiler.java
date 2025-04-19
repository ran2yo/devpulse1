package com.example.devpulse1.agent;

import com.example.devpulse1.log.LogEntity;
import com.example.devpulse1.log.LogWriter;

public class MethodProfiler {
    public static void start(String methodName) {
        CallContext.enter(methodName);
        ProfilingState.setStart(System.nanoTime());
    }

    public static void end(String methodName) {
        long end = System.nanoTime();
        long duration = end - ProfilingState.getStart();
        double elapsedMs = duration / 1_000_000.0;

        LogWriter.write(new LogEntity(
                methodName,
                elapsedMs,
                CallContext.getDepth(),
                CallContext.getCurrentTrace()
        ));

        CallContext.exit();
        ProfilingState.clear();
    }
}
