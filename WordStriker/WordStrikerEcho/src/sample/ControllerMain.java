package sample;

import javafx.fxml.FXML;

public class ControllerMain {

    public static int PORT = 4444;

    @FXML
    private void connect() {
        SocketServer S = new SocketServer();

        S.connect(new String[] {"" + PORT});
    }
}
