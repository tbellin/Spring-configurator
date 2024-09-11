package org.jbelt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringConfigurator {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar Spring-configurator-1.0-SNAPSHOT.jar <envFilePath> <ymlFilePath1> [<ymlFilePath2>] [--restore]");
            return;
        }

        String envFilePath = args[0];
        String ymlFilePath1 = args[1];
        String ymlFilePath2 = args.length > 2 && !args[2].equals("--restore") ? args[2] : null;
        boolean restore = args.length > 2 && args[args.length - 1].equals("--restore");

        if (restore) {
            restoreBackup(ymlFilePath1);
            if (ymlFilePath2 != null) {
                restoreBackup(ymlFilePath2);
            }
            System.out.println("Files restored from backup.");
            return;
        }

        // Verifica l'esistenza dei file
        if (!Files.exists(Paths.get(envFilePath))) {
            System.out.println("Error: .env file not found at " + envFilePath);
            return;
        }

        if (!Files.exists(Paths.get(ymlFilePath1))) {
            System.out.println("Error: YAML file not found at " + ymlFilePath1);
            return;
        }

        if (ymlFilePath2 != null && !Files.exists(Paths.get(ymlFilePath2))) {
            System.out.println("Error: YAML file not found at " + ymlFilePath2);
            return;
        }

        // Crea backup dei file YAML
        createBackup(ymlFilePath1);
        if (ymlFilePath2 != null) {
            createBackup(ymlFilePath2);
        }

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

    private static void createBackup(String filePath) throws IOException {
        Path source = Paths.get(filePath);
        Path backup = Paths.get(filePath + ".bak");
        Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void restoreBackup(String filePath) throws IOException {
        Path backup = Paths.get(filePath + ".bak");
        Path original = Paths.get(filePath);
        if (Files.exists(backup)) {
            Files.copy(backup, original, StandardCopyOption.REPLACE_EXISTING);
        } else {
            System.out.println("Backup not found for " + filePath);
        }
    }
}
