package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

class SenderLabel extends Label {
    public SenderLabel(String s) {
        super(s);
        this.setStyle(("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;" +
                "-fx-padding: 5px; -fx-border-insets: 5px;" +
                "-fx-background-color: rgb(51,153,255);"));
        //this.setAlignment(Pos.TOP_RIGHT);
    }
}

class ReceiverLabel extends Label {
    public ReceiverLabel(String s){
        super(s);
        this.setStyle(("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 20px;" +
        "-fx-padding: 5px; -fx-border-insets: 5px;" +
                "-fx-background-color: rgb(153,0,204);"));
    }
}