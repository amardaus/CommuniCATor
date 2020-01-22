package sample;

import com.sun.tools.javac.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Random;

class AV{
    static int recAV = (new Random().nextInt(6));
    static int sendAV = (new Random().nextInt(6));
}

class SenderLabel extends HBox {
    public SenderLabel(String s) {
        super();
        this.setSpacing(10);

        String path = "/sample/img/c" + AV.sendAV + ".jpg";
        Image image = new Image(this.getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        this.getChildren().add(imageView);

        Label label = new Label(s);
        label.setStyle(("-fx-text-fill: white; -fx-font-size: 20px;" +
                "-fx-padding: 5px; -fx-border-insets: 5px;" +
                "-fx-background-color: rgb(51,153,255);"));
        this.getChildren().add(label);
    }
}

class ReceiverLabel extends HBox {
    public ReceiverLabel(String s) {
        super();
        this.setSpacing(10);

        String path = "/sample/img/c" + AV.recAV + ".jpg";
        Image image = new Image(this.getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        this.getChildren().add(imageView);

        Label label = new Label(s);
        label.setStyle(("-fx-text-fill: white; -fx-font-size: 20px;" +
                "-fx-padding: 5px; -fx-border-insets: 5px;" +
                "-fx-background-color: rgb(153,0,204);"));
        this.getChildren().add(label);
    }
}
