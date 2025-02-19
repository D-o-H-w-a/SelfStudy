module org.example.honeyz_streaming_alert {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.honeyz_streaming_alert to javafx.fxml;
    exports org.example.honeyz_streaming_alert;
}