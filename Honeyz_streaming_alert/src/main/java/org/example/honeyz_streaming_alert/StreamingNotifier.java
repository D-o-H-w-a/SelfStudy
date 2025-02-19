package org.example.honeyz_streaming_alert;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StreamingNotifier extends Application {

    private static final List<String> channelId = Arrays.asList(
        "b82e8bc2505e37156b2d1140ba1fc05c", // 담유이
        "bd07973b6021d72512240c01a386d5c9", // 망내
        "65a53076fe1a39636082dd6dba8b8a4b" // 오화요
    );

    private static final String api_url = "https://api.chzzk.naver.com/service/v1/channels/{channel_id}";
    private final boolean[] isStreamingLive = new boolean[channelId.size()];  // 각 채널 상태 관리

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("허니즈 방송 알리미");
        stage.show();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStreamingStatuses();
            }
        }, 0, 10000);
    }

    private void checkStreamingStatuses() {

        for (String str : channelId) {
            String channel = str;
            String apiUrl = api_url.replace("{channel_id}", channel);
            try {
                HttpURLConnection connect = (HttpURLConnection) new URL(apiUrl).openConnection();
                connect.setRequestMethod("GET");
                connect.setRequestProperty("Accept", "application/json");
                connect.setConnectTimeout(5000);
                connect.setReadTimeout(5000);

                int responseCode = connect.getResponseCode();
                if (responseCode == 200) {  // 정상 응답
                    BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // JSON 데이터 파싱
                    String responseString = response.toString();
                    if (isLiveStreaming(responseString) && !isStreamingLive[i]) {
                        isStreamingLive[i] = true;  // 상태 변경
                        showWindowsNotification("Naver Broadcast is now live!",
                                "The broadcast for channel " + channelId + " has started.");
                    } else if (!isLiveStreaming(responseString)) {
                        isStreamingLive[i] = false;  // 방송이 꺼졌을 경우 상태 초기화
                    }
                }
             } catch (Exception e) {
                System.err.println("해당 채널의 방송 상태를 확인하지 못하였습니다. " + channelId + ": " + e.getMessage());
            }
        }
    }

    // JSON 문자열에서 "openLive" 필드가 true인지 확인
    private boolean isLiveStreaming(String jsonString) {
        return jsonString.contains("\"openLive\":true");
    }


    public static void main(String[] args) {
        launch();
    }
}