package com.example.devpulse1.log;

import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static  String logPath  = "logs/devpulse.json";

    public static void setLogPath(String path) {
        logPath = path;
    }
        public static synchronized void write(LogEntity entry) {
        try {
            new File("logs").mkdirs(); // ensure dir
            try (PrintWriter out = new PrintWriter(new FileWriter(logPath, true))) {
                out.println(mapper.writeValueAsString(entry));
            }
        } catch (IOException e) {
            System.err.println("[DevPulse] 로그 저장 실패: " + e.getMessage());
        }
    }
}