package com.example.devpulse1.agent;


import com.example.devpulse1.log.LogWriter;

import java.lang.instrument.ClassFileTransformer;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

public class DevPulseAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[DevPulseAgent] Agent initialized.");
        Map<String, String> config = parseAgentArgs(agentArgs);
        if(config.containsKey("logPath")) {
            LogWriter.setLogPath(config.get("logPath"));
        }
        if(config.containsKey("include")) {
            ClassTransformer.setIncludePrefixes(config.get("include"));
        }
        if (config.containsKey("exclude")) {
            ClassTransformer.setExcludePrefixes(config.get("exclude"));
        }

        inst.addTransformer(new ClassTransformer());
    }

    private static Map<String, String> parseAgentArgs(String args) {
        Map<String, String> map = new HashMap<>();
        if (args == null || args.isEmpty()) return map;

        String[] pairs = args.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }
}
