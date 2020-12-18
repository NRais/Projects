package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ControllerMain {

    public static int PORT = 4444;
    private SocketClient s;

    @FXML
    private TextArea textField;

    @FXML
    private void connect() {
        s = new SocketClient();


        // put into a thread so the gui still runs while we are listening
        Thread one = new Thread(() -> s.connect("localhost", PORT, textField));

        one.start();
    }

    @FXML
    private void send() {
        if (s != null) {
            s.send(textField.getText());
        }
    }
}
