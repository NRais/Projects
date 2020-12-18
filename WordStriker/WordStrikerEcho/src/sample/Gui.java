package sample;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        StackPane stackPane = new StackPane();

        // SHAPES AND TEXT SCENE
        // IMAGE
        Image fxImage = new Image(getClass().getResourceAsStream("/image.png"), 100, 100, false, false);
        ImageView selectedImage = new ImageView();
        selectedImage.setImage(fxImage);
        rotateThis(selectedImage);

        // ELLIPSE
        Stop[] stops = new Stop[] { new Stop(0, Color.DODGERBLUE), new Stop(0.4, Color.LIGHTBLUE), new Stop(1, Color.LIGHTGREEN)};
        Ellipse ellipse = new Ellipse(110, 70);
        ellipse.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops));
        ellipse.setEffect(new DropShadow(30, 10, 10, Color.GRAY));
        rotateThis(ellipse);

        // ROTATION EFFECT
        Text text = new Text("My Shapes");
        text.setFont(new Font("Arial Bold", 24));
        Reflection r = new Reflection();
        r.setFraction(0.8);
        text.setEffect(r);
        rotateThis(text);


        // SCENE FROM FXML FILE
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        rotateThis(root);

        stackPane.getChildren().addAll(root, ellipse, text, selectedImage);

        Scene scene = new Scene(stackPane, 300, 275, Color.BLACK); // note 300x275 is the small size when not maximized

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void rotateThis(Node thisObject) {
        RotateTransition rotate = new RotateTransition(Duration.millis(500), thisObject);
        rotate.setToAngle(360);
        rotate.setFromAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);

        thisObject.setOnMouseClicked(mouseEvent -> {
            if (rotate.getStatus().equals(Animation.Status.RUNNING)) {
                rotate.pause();
            } else {
                rotate.play();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
