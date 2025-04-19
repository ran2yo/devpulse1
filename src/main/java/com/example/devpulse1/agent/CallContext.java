package com.example.devpulse1.agent;

import java.util.ArrayDeque;
import java.util.Deque;

public class CallContext {
    private static final ThreadLocal<Deque<String>> callStack = ThreadLocal.withInitial(ArrayDeque::new);

    public static void  enter(String methodName) {
        callStack.get().push(methodName);
    }

    public static void  exit() {
        Deque<String> stack = callStack.get();
        if (stack.isEmpty()) {
            stack.pop();
        }
    }
    public static String getCurrentTrace() {
        return String.join("-> ", callStack.get());
    }
    public static int getDepth() {
        return callStack.get().size();
    }
}


