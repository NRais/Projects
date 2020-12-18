package sample;

import javafx.fxml.FXML;

public class ControllerMain {

    public static int PORT = 4444;

    @FXML
    private void connect() {
        SocketClient s = new SocketClient();
        s.connect(new String[] {"localhost", "" + PORT});
    }
}
