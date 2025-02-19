package org.example.honeyz_streaming_alert;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

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
    private CheckBox autoStartCheckBox;

    private TrayIcon trayIcon;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("허니즈 방송 알리미");

        strmingkeyReset();

        stateManager = new CheckBoxStateManager(CONFIG_FILE);
        stateManager.loadCheckBoxStates(autoOpenEnabled, "_Open");
        stateManager.loadCheckBoxStates(notifyEnabled, "_Nofi");

        // 추가된 부분: 윈도우 시작 시 자동 실행 설정을 위한 체크박스 생성
        autoStartCheckBox = new CheckBox("윈도우 시작 시 자동 실행");
        boolean isAutoStartEnabled = stateManager.loadAutoStartState();
        autoStartCheckBox.setSelected(isAutoStartEnabled);

        // 체크박스 상태 변경 시 자동 실행 설정을 업데이트
        autoStartCheckBox.setOnAction(event -> {
            boolean selected = autoStartCheckBox.isSelected();
            setAutoStart(selected);
            stateManager.saveAutoStartState(selected);
        });

        GridPane streamingShortcutGrid = CheckBoxGridCreator.createStreamingShortcutGrid(autoOpenEnabled);
        GridPane streamingNotificationGrid = CheckBoxGridCreator.createStreamingNotificationGrid(notifyEnabled);

        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(20);
        mainGrid.setAlignment(javafx.geometry.Pos.CENTER);
        mainGrid.add(streamingShortcutGrid, 0, 0);
        mainGrid.add(streamingNotificationGrid, 0, 1);
        mainGrid.add(autoStartCheckBox, 0, 2); // 체크박스를 UI에 추가
        GridPane.setHalignment(autoStartCheckBox, HPos.CENTER); // 중앙 정렬
        GridPane.setValignment(autoStartCheckBox, VPos.CENTER); // 중앙 정렬

        Scene scene = new Scene(mainGrid, 350, 300);
        stage.setScene(scene);
        stage.show();

        // 윈도우 시작 시 자동으로 실행되도록 설정
        if (isAutoStartEnabled) {
            ensureAutoStart();
        } else {
            removeAutoStart();
        }

        // 시스템 트레이 아이콘 설정
        setupSystemTray(stage);

        // 창 닫기 요청 처리
        stage.setOnCloseRequest(event -> {
            event.consume();
            showExitOrTrayDialog(stage);
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStreamingStatuses();
            }
        }, 0, 1000);
    }

    private void strmingkeyReset() {
        for (String channelName : channelId.keySet()) {
            isStreamingLive.put(channelName, false);
            autoOpenEnabled.put(channelName + "_Open", false);
            notifyEnabled.put(channelName + "_Nofi", false);
        }
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
                    if (isLive && !isStreamingLive.getOrDefault(channelName, false)) {
                        isStreamingLive.put(channelName, true);
                        if (notifyEnabled.getOrDefault(channelName + "_Nofi", false)) {
                            showWindowsNotification("방송 알림", channelName + "의 방송이 라이브 상태입니다!",
                                    "https://chzzk.naver.com/live/" + channelId);
                        }
                        if (autoOpenEnabled.getOrDefault(channelName + "_Open", false)) {
                            openUrlInBrowser("https://chzzk.naver.com/live/" + channelId);
                        }
                    } else if (!isLive && isStreamingLive.getOrDefault(channelName, false)) {
                        isStreamingLive.put(channelName, false);
                    }
                }
            } catch (Exception e) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                System.err.println("Failed to check streaming status for channel " + channelName + ": " + errorMessage);
                e.printStackTrace();
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

    // 추가된 부분: 윈도우 시작 시 자동 실행 설정을 레지스트리에 추가
    private void ensureAutoStart() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        String key = "StreamingNotifier";
        String value = "\"" + System.getProperty("java.home") + "\\bin\\javaw.exe\" -jar \"" + new java.io.File(StreamingNotifier.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath() + "\"";

        try {
            if (prefs.get(key, null) == null) {
                prefs.put(key, value);
                System.out.println("Auto-start entry added to Windows registry.");
            } else {
                System.out.println("Auto-start entry already exists in Windows registry.");
            }
        } catch (Exception e) {
            System.err.println("Failed to modify Windows startup settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 추가된 부분: 윈도우 시작 시 자동 실행 설정을 레지스트리에서 제거
    private void removeAutoStart() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        String key = "StreamingNotifier";

        try {
            if (prefs.get(key, null) != null) {
                prefs.remove(key);
                System.out.println("Auto-start entry removed from Windows registry.");
            }
        } catch (Exception e) {
            System.err.println("Failed to modify Windows startup settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 추가된 부분: 윈도우 시작 시 자동 실행 설정을 업데이트
    private void setAutoStart(boolean enable) {
        if (enable) {
            ensureAutoStart();
        } else {
            removeAutoStart();
        }
    }

    private void hideToSystemTray(Stage stage) {
        stage.setOpacity(0); // 창을 보이지 않게 함
    }

    private void setupSystemTray(Stage stage) {
        if (!SystemTray.isSupported()) {
            System.err.println("시스템 트레이를 지원하지 않습니다.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/Honeyz_logo.jpg"));

        trayIcon = new TrayIcon(image, "방송 알리미");
        trayIcon.setImageAutoSize(true);

        PopupMenu popupMenu = new PopupMenu();
        Font menuFont = new Font("Malgun Gothic", Font.PLAIN, 12); // 한글 지원 폰트

        popupMenu.setFont(menuFont); // PopupMenu 전체에 폰트 설정

        MenuItem openItem = new MenuItem("열기");
        openItem.setFont(menuFont); // 메뉴 항목에 폰트 설정
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(() -> {
                    stage.setOpacity(1); // 창을 다시 보이게 설정
                    stage.show();
                    stage.toFront(); // 창을 최상단으로 올리기
                    stage.requestFocus(); // 포커스를 요청
                });
            }
        });
        popupMenu.add(openItem);

        MenuItem exitItem = new MenuItem("종료");
        exitItem.setFont(menuFont); // 메뉴 항목에 폰트 설정
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                Platform.exit();
                System.exit(0);
            }
        });
        popupMenu.add(exitItem);

        trayIcon.setPopupMenu(popupMenu);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("트레이 아이콘 추가 실패: " + e.getMessage());
        }
    }

    private void showExitOrTrayDialog(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("종료 확인");
        alert.setHeaderText(null);
        alert.setContentText("프로그램을 종료하거나 시스템 트레이로 이동하시겠습니까?");

        ButtonType trayButton = new ButtonType("시스템 트레이로 이동");
        ButtonType exitButton = new ButtonType("종료");
        ButtonType cancelButton = new ButtonType("취소", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(trayButton, exitButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == trayButton) {
            hideToSystemTray(stage);
        } else if (result.isPresent() && result.get() == exitButton) {
            Platform.exit();
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}