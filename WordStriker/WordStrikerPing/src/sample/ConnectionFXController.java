package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ConnectionFXController {

    public static int PORT = 4444;
    private SocketClient sc;
    private SocketServer ss;

    private boolean CLIENT = false;

    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;

    @FXML
    private Pane root;
    @FXML
    private Pane transitionPane;

    @FXML
    private void connect() {

        sendButton.setDefaultButton(true);
        sendButton.setOnAction(e -> send());

        if (CLIENT) {
            sc = new SocketClient();
            // put into a thread so the gui still runs while we are listening
            Thread one = new Thread(() -> sc.connect("localhost", PORT, this));

            one.start();
        } else {
            ss = new SocketServer();
            // put into a thread so the gui still runs while we are listening
            Thread one = new Thread(() -> ss.connect(PORT, this));

            one.start();
        }
    }

    @FXML
    private void send() {
        if (CLIENT) {
            if (sc != null) {
                sc.send(textField.getText());
            }
        } else {
            if (ss != null) {
                ss.send(textField.getText());
            }
        }
    }

    @FXML
    public void printText(String s) {
        Text msg = new Text(s);
        msg.setY(50);
        msg.setTextOrigin(VPos.CENTER);
        msg.setFont(Font.font(24));
        msg.setFill(Color.WHITE);

        transitionPane.getChildren().add(msg);

        Scene scene = root.getScene();

        double sceneWidth = scene.getWidth();

        KeyValue initKeyValue = new KeyValue(msg.translateXProperty(), 0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);

        KeyValue endKeyValue = new KeyValue(msg.translateXProperty(), sceneWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(3), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);

        timeline.setCycleCount(1);
        timeline.play();
    }
}
