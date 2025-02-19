package org.example.honeyz_streaming_alert;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StreamingNotifier extends Application {

    private static final Map<String, String> channelId = new HashMap<>() {{
        put("오화요", "65a53076fe1a39636082dd6dba8b8a4b");
        put("허니츄러스", "c0d9723cbb75dc223c6aa8a9d4f56002");
        put("담유이", "b82e8bc2505e37156b2d1140ba1fc05c");
        put("디디디용", "798e100206987b59805cfb75f927e965");
        put("아야", "abe8aa82baf3d3ef54ad8468ee73e7fc");
        put("망내", "bd07973b6021d72512240c01a386d5c9");
    }};

    private Map<String, Boolean> autoOpenEnabled = new HashMap<>();
    private Map<String, Boolean> notifyEnabled = new HashMap<>();
    private static final String CONFIG_FILE = "checkbox_status.properties";
    private CheckBoxStateManager stateManager;

    private static final String api_url = "https://api.chzzk.naver.com/service/v1/channels/{channel_id}";

    private final Map<String, Boolean> isStreamingLive = new HashMap<>();

    public void strmingkeyReset() {
        for (String channelName : channelId.keySet()) {
            isStreamingLive.put(channelName, false);
            autoOpenEnabled.put(channelName + "_Open", false);
            notifyEnabled.put(channelName + "_Nofi", false);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("허니즈 방송 알리미");

        strmingkeyReset();

        stateManager = new CheckBoxStateManager(CONFIG_FILE);
        stateManager.loadCheckBoxStates(autoOpenEnabled, "_Open");
        stateManager.loadCheckBoxStates(notifyEnabled, "_Nofi");

        GridPane streamingShortcutGrid = CheckBoxGridCreator.createStreamingShortcutGrid(autoOpenEnabled);
        GridPane streamingNotificationGrid = CheckBoxGridCreator.createStreamingNotificationGrid(notifyEnabled);

        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(20);
        mainGrid.setAlignment(javafx.geometry.Pos.CENTER);
        mainGrid.add(streamingShortcutGrid, 0, 0);
        mainGrid.add(streamingNotificationGrid, 0, 1);

        Scene scene = new Scene(mainGrid, 350, 250);
        stage.setScene(scene);
        stage.show();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStreamingStatuses();
            }
        }, 0, 1000);
    }

    private void checkStreamingStatuses() {
        for (Map.Entry<String, String> entry : channelId.entrySet()) {
            String channelName = entry.getKey();
            String channelId = entry.getValue();
            String apiUrl = api_url.replace("{channel_id}", channelId);

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    boolean isLive = isLiveStreaming(response.toString());
                    if (isLive && !isStreamingLive.get(channelName)) {
                        isStreamingLive.put(channelName, true);
                        if (notifyEnabled.getOrDefault(channelName + "_Nofi", false)) {
                            showWindowsNotification("방송 알림", channelName + "의 방송이 라이브 상태입니다!",
                                    "https://chzzk.naver.com/live/" + channelId);
                        }
                        if (autoOpenEnabled.getOrDefault(channelName + "_Open", false)) {
                            openUrlInBrowser("https://chzzk.naver.com/live/" + channelId);
                        }
                    } else if (!isLive && isStreamingLive.get(channelName)) {
                        isStreamingLive.put(channelName, false);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to check streaming status for channel " + channelName + ": " + e.getMessage());
            }
        }
    }

    private boolean isLiveStreaming(String jsonString) {
        return jsonString.contains("\"openLive\":true");
    }

    private void showWindowsNotification(String title, String message, String url) {
        if (!SystemTray.isSupported()) {
            System.err.println("시스템 트레이를 지원하지 않습니다.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "방송 알리미");
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("트레이 아이콘 추가 실패: " + e.getMessage());
            return;
        }

        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tray.remove(trayIcon);
            }
        }, 500);
    }

    private void openUrlInBrowser(String url) {
        try {
            System.out.println("URL 열기 시도: " + url);
            Desktop.getDesktop().browse(new URI(url));  // 브라우저에서 URL 열기
        } catch (Exception ex) {
            System.err.println("URL 열기 실패: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}