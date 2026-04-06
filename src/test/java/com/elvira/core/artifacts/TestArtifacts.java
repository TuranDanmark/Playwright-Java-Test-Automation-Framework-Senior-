package com.elvira.core.artifacts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestArtifacts {

    private static final ThreadLocal<List<String>> consoleLogs =
            ThreadLocal.withInitial(ArrayList::new);

    private static final ThreadLocal<List<String>> networkLogs =
            ThreadLocal.withInitial(ArrayList::new);

    private static final ThreadLocal<List<String>> errors =
            ThreadLocal.withInitial(ArrayList::new);

    private static final int MAX_LOG_SIZE = 5000; // защита от OutOfMemory в CI

    // 🧾 Console logs
    public static void addConsoleLog(String log) {
        addLog(consoleLogs, log, "Console");
    }

    public static List<String> getConsoleLogs() {
        return consoleLogs.get();
    }

    // 📡 Network logs
    public static void addNetworkLog(String log) {
        addLog(networkLogs, log, "Network");
    }

    public static List<String> getNetworkLogs() {
        return networkLogs.get();
    }

    // ❗ JS / runtime errors
    public static void addError(String error) {
        addLog(errors, error, "Error");
    }

    public static List<String> getErrors() {
        return errors.get();
    }

    // 🧹 очистка после теста
    public static void clear() {
        consoleLogs.remove();
        networkLogs.remove();
        errors.remove();
        log("Artifacts cleared for thread: " + Thread.currentThread().getName());
    }

    // 💾 дамп в файл (Allure / debug)
    public static void dumpToFile(String testId, String folder) {
        try {
            File dir = new File(folder);
            if (!dir.exists()) dir.mkdirs();

            dumpList(consoleLogs.get(), folder + "/" + testId + "_console.log");
            dumpList(networkLogs.get(), folder + "/" + testId + "_network.log");
            dumpList(errors.get(), folder + "/" + testId + "_errors.log");

            log("Artifacts dumped to folder: " + folder);
        } catch (Exception e) {
            log("Failed to dump artifacts: " + e.getMessage());
        }
    }

    // 🔧 helper для добавления с контролем размера
    private static void addLog(ThreadLocal<List<String>> threadList, String log, String type) {
        List<String> list = threadList.get();
        if (list.size() > MAX_LOG_SIZE) {
            list.remove(0); // удаляем старые записи
        }
        list.add(log);
        log(type + " log added: " + log);
    }

    private static void dumpList(List<String> list, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (String s : list) {
                writer.write(s + "\n");
            }
        }
    }

    private static void log(String message) {
        System.out.println("[TestArtifacts] " + message);
    }
}