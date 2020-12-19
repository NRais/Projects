package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenuFXController {

    private int count = 0 ;

    @FXML
    private Button incrementButton;
    @FXML
    private Button switchPaneButton;

    @FXML
    private void increment() {
        count++;
        incrementButton.setText("C "+count);
    }

    @FXML
    private void paneswitch() {

        // get the current stage
        Stage stage = (Stage) switchPaneButton.getScene().getWindow();

        // load the game fxml file
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(root);

            Scene scene = new Scene(stackPane, Color.BLACK);

            // Swap screen
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}