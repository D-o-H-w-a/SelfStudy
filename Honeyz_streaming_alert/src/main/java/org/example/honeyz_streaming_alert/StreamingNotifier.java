package org.example.honeyz_streaming_alert;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

public class StreamingNotifier extends Application {

    private static final Map<String, String> channelId = new HashMap<>() {{
        put("담유이" ,"b82e8bc2505e37156b2d1140ba1fc05c"); // 담유이
        put("망내" ,"bd07973b6021d72512240c01a386d5c9"); // 망내
        put("오화요" ,"65a53076fe1a39636082dd6dba8b8a4b"); // 오화요
    }};

    private static final String api_url = "https://api.chzzk.naver.com/service/v1/channels/{channel_id}";

    // 각 채널의 상태를 관리하는 Map (초기 값은 모두 false)
    private final Map<String, Boolean> isStreamingLive = new HashMap<>();

    // 초기화 시 모든 채널 상태를 false로 설정
    public void strmingkeyReset() {
        for (String channelName : channelId.keySet()) {
            isStreamingLive.put(channelName, false);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("허니즈 방송 알리미");
        stage.show();
        strmingkeyReset();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStreamingStatuses();
            }
        }, 0, 10000);
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
                        // 상태가 변경됨: 방송이 켜짐
                        isStreamingLive.put(channelName, true);
                        showWindowsNotification("방송 알림",   channelName + "의 방송이 라이브 상태입니다!");
                    } else if (!isLive && isStreamingLive.get(channelName)) {
                        // 상태가 변경됨: 방송이 꺼짐
                        isStreamingLive.put(channelName, false);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to check broadcast status for channel " + channelName + ": " + e.getMessage());
            }
        }
    }

    // JSON 문자열에서 "openLive" 필드가 true인지 확인
    private boolean isLiveStreaming(String jsonString) {
        return jsonString.contains("\"openLive\":true");
    }

    private void showWindowsNotification(String title, String message) {
        // Java AWT 트레이 아이콘을 사용할 수 있는지 확인
        if (!SystemTray.isSupported()) {
            System.err.println("시스템 트레이를 지원하지 않습니다.");
            return;
        }

        // 트레이 아이콘 생성
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");  // 알림 아이콘 설정 (없으면 기본 사용)

        TrayIcon trayIcon = new TrayIcon(image, "방송 알리미");
        trayIcon.setImageAutoSize(true);

        // 트레이 아이콘 추가
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("트레이 아이콘 추가 실패: " + e.getMessage());
            return;
        }

        // 알림 표시
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);

        // 알림 표시 후 1초 뒤 트레이 아이콘 제거
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tray.remove(trayIcon);
            }
        }, 1000);  // 1초 후 트레이 아이콘 제거
    }

    public static void main(String[] args) {
        launch();
    }
}