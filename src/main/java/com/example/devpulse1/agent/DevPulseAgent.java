package com.example.devpulse1.agent;


import java.lang.instrument.ClassFileTransformer;

import java.lang.instrument.Instrumentation;

public class DevPulseAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[DevPulseAgent] Agent initialized.");
        inst.addTransformer(new ClassTransformer());
    }
}
