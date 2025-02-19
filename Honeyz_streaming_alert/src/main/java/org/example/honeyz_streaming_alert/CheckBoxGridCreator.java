package org.example.honeyz_streaming_alert;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class CheckBoxGridCreator {

    // 방송 바로가기 레이아웃 생성
    public static GridPane createStreamingShortcutGrid(Map<String, Boolean> autoOpenEnabled) {
        return createCheckBoxGrid(autoOpenEnabled, "방송 바로가기");
    }

    // 방송 알림 레이아웃 생성
    public static GridPane createStreamingNotificationGrid(Map<String, Boolean> nofityEnabled) {
        return createCheckBoxGrid(nofityEnabled, "방송 알림");
    }

    // 공통 체크박스 그리드 생성 메서드
    private static GridPane createCheckBoxGrid(Map<String, Boolean> enabledStatus, String title) {
        // GridPane 설정
        GridPane grid = new GridPane();
        grid.setVgap(10);  // 세로 간격
        grid.setHgap(10);  // 가로 간격
        grid.setAlignment(javafx.geometry.Pos.CENTER);  // 중앙 정렬

        // 제목 추가
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI'; -fx-text-fill: #333333;");

        // 제목을 첫 번째 행에 추가
        grid.add(titleLabel, 0, 0, 3, 1);  // 3개 칸을 차지하도록 설정

        // 제목 중앙 정렬 설정
        GridPane.setHalignment(titleLabel, HPos.CENTER);  // 수평 중앙 정렬
        GridPane.setValignment(titleLabel, VPos.CENTER);  // 수직 중앙 정렬

        // Map의 항목을 순회하며 체크박스 생성
        int row = 1;  // 첫 번째 행은 제목이 차지하므로 두 번째 행부터 시작
        int col = 0;

        for (Map.Entry<String, Boolean> entry : enabledStatus.entrySet()) {
            CheckBox checkBox = new CheckBox(entry.getKey().split("_")[0]);
            checkBox.setSelected(entry.getValue());

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                enabledStatus.put(entry.getKey(), newValue);
                CheckBoxStateManager.getInstance().saveCheckBoxStates(enabledStatus, entry.getKey().contains("_Open") ? "_Open" : "_Nofi");
            });

            // 체크박스를 GridPane에 추가
            grid.add(checkBox, col, row);

            // 한 줄에 3개씩 배치 (3개씩 배치 후, 다음 줄로 넘어가기)
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        return grid;
    }
}
