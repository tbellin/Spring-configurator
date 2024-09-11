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
        if (args.length < 2) {
            System.out.println("Usage: java SpringConfigurator <envFilePath> <ymlFilePath1> [<ymlFilePath2>]");
            return;
        }

        String envFilePath = args[0]; // File .env specificato in run string
        String ymlFilePath1 = args[1]; // Primo file di configurazione specificato in run string
        String ymlFilePath2 = args.length > 2 ? args[2] : null; // Secondo file di configurazione (opzionale)

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
