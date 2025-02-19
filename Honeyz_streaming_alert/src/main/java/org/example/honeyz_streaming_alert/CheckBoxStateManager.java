package org.example.honeyz_streaming_alert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public class CheckBoxStateManager {

    private static CheckBoxStateManager instance;
    private final String configFilePath;

    public static CheckBoxStateManager getInstance() {
        return instance;
    }

    public CheckBoxStateManager(String configFilePath) {
        this.configFilePath = configFilePath;
        instance = this;
    }

    public void loadCheckBoxStates(Map<String, Boolean> checkBoxStates, String suffix) {
        Properties properties = new Properties();
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
                properties.load(inputStream);
                for (String key : checkBoxStates.keySet()) {
                    if (key.endsWith(suffix)) {
                        checkBoxStates.put(key, Boolean.parseBoolean(properties.getProperty(key, "false")));
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to load checkbox states: " + e.getMessage());
            }
        } else {
            createDefaultConfigFile(checkBoxStates, suffix);
        }
    }

    public void saveCheckBoxStates(Map<String, Boolean> checkBoxStates, String suffix) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load existing properties: " + e.getMessage());
        }

        try (FileOutputStream outputStream = new FileOutputStream(configFilePath)) {
            for (Map.Entry<String, Boolean> entry : checkBoxStates.entrySet()) {
                if (entry.getKey().endsWith(suffix)) {
                    properties.setProperty(entry.getKey(), entry.getValue().toString());
                }
            }
            properties.store(outputStream, null);
        } catch (IOException e) {
            System.err.println("Failed to save checkbox states: " + e.getMessage());
        }
    }

    private void createDefaultConfigFile(Map<String, Boolean> checkBoxStates, String suffix) {
        Properties properties = new Properties();
        for (String key : checkBoxStates.keySet()) {
            if (key.endsWith(suffix)) {
                properties.setProperty(key, "false");
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(configFilePath)) {
            properties.store(outputStream, null);
            System.out.println("Default config file created: " + configFilePath);
        } catch (IOException e) {
            System.err.println("Failed to create default config file: " + e.getMessage());
        }
    }

    // 추가된 부분: 자동 시작 설정을 로드
    public boolean loadAutoStartState() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
            properties.load(inputStream);
            return Boolean.parseBoolean(properties.getProperty("autoStart", "false"));
        } catch (IOException e) {
            System.err.println("Failed to load auto start state: " + e.getMessage());
            return false;
        }
    }

    // 추가된 부분: 자동 시작 설정을 저장
    public void saveAutoStartState(boolean state) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load existing properties: " + e.getMessage());
        }

        try (FileOutputStream outputStream = new FileOutputStream(configFilePath)) {
            properties.setProperty("autoStart", Boolean.toString(state));
            properties.store(outputStream, null);
        } catch (IOException e) {
            System.err.println("Failed to save auto start state: " + e.getMessage());
        }
    }
}