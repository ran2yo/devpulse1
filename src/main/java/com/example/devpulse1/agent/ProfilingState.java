package com.example.devpulse1.agent;

public class ProfilingState {
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public static void setStart(long start) {
        startTime.set(start);
    }

    public static long getStart() {
        Long time = startTime.get();
        return (time != null) ? time : 0L;
    }

    public static void clear() {
        startTime.remove();
    }
}
