package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerMain {

    public static int PORT = 4444;
    private SocketServer S;

    @FXML
    private TextField textField;

    @FXML
    private void connect() {
        S = new SocketServer();

        // put into a thread so the gui still runs while we are listening
        Thread one = new Thread(() -> S.connect(PORT, textField));

        one.start();
    }

    @FXML
    private void send() {
        if (S != null) {
            S.send(textField.getText());
        }
    }
}
