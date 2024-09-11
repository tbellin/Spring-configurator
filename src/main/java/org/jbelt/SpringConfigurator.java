package org.jbelt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringConfigurator {
    public static void main(String[] args) throws IOException {
        String envFilePath = ".env"; // File .env nella radice del progetto
        String ymlFilePath1 = System.getProperty("config.file1"); // Primo file di configurazione
        String ymlFilePath2 = System.getProperty("config.file2"); // Secondo file di configurazione (opzionale)

        Map<String, String> envVariables = loadEnvVariables(envFilePath);

        if (ymlFilePath1 != null && !ymlFilePath1.isEmpty()) {
            replaceVariablesInYml(ymlFilePath1, envVariables);
        }

        if (ymlFilePath2 != null && !ymlFilePath2.isEmpty()) {
            replaceVariablesInYml(ymlFilePath2, envVariables);
        }
    }

    private static Map<String, String> loadEnvVariables(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return lines.stream()
                .filter(line -> line.contains("="))
                .map(line -> line.split("=", 2))
                .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
    }

    private static void replaceVariablesInYml(String filePath, Map<String, String> envVariables) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<String> updatedLines = lines.stream()
                .map(line -> {
                    for (Map.Entry<String, String> entry : envVariables.entrySet()) {
                        line = line.replace("${" + entry.getKey() + "}", entry.getValue());
                    }
                    return line;
                })
                .collect(Collectors.toList());
        Files.write(Paths.get(filePath), updatedLines);
    }
}
