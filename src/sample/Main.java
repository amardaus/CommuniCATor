package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    String nickname;
    String receiver;
    SendMessage smsg;

    void initializeCommunication(){
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 59090);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        smsg = new SendMessage(socket, nickname, "B");
        Thread sendMessage = new Thread(smsg);
        //zamienic receiver zeby sie dalo ustawiac nie tylko przy tworzeniu!

        Thread readMessage = new Thread(new ReadMessage(socket));

        readMessage.start();
        sendMessage.start();

    }

    ListView<String> getUserList(){
        ListView<String> userList = new ListView<>();
        //pobieram liste klientow z servera
        userList.getItems().addAll("A", "B", "C");
        return userList;
    }

    @Override
    public void start(Stage primaryStage){
        Stage nicknameDialog = new Stage();
        Stage userDialog = new Stage();
        Stage chatDialog = new Stage();

        Button nicknameBtn = new Button("Ok");
        Text enterNickname = new Text(50, 50, "Enter your nickname");
        enterNickname.setFont(Font.font(20));
        TextField nicknameField = new TextField();

        nicknameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nickname = nicknameField.getText();
                nicknameDialog.close();
                userDialog.show();
            }
        });

        VBox nicknameVBox = new VBox();
        nicknameVBox.getChildren().addAll(enterNickname, nicknameField, nicknameBtn);
        Scene nicknameScene = new Scene(nicknameVBox, 300, 300);
        VBox userListBox = new VBox();
        Scene userListDialog = new Scene(userListBox, 300, 300);
        userDialog.setScene(userListDialog);
        nicknameDialog.setScene(nicknameScene);
        ListView<String> userList = getUserList();
        userListBox.getChildren().addAll(userList);
        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userDialog.close();
                chatDialog.show();
                initializeCommunication();
            }
        });

        BorderPane chatContainer = new BorderPane();        //main pane
        VBox messagesContainer = new VBox();
        TextField messageField = new TextField();

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!messageField.getText().isEmpty()){
                    //pass value to client.sendmessage
                    smsg.setLine(messageField.getText());
                }
            }
        });



        HBox bottomContainer = new HBox();


        ArrayList<Label> messages = new ArrayList<>();
        messages.add(new Label("hello"));
        messages.add(new Label("hi"));
        messages.add(new Label("how are u?"));
        messages.add(new Label("fine, thx"));

        messagesContainer.getChildren().addAll(messages);
        bottomContainer.setAlignment(Pos.BOTTOM_CENTER);

        //multiple messages instead of single textfield

        bottomContainer.getChildren().addAll(messageField, sendBtn);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        chatContainer.setTop(messagesContainer);
        chatContainer.setBottom(bottomContainer);

        Scene chatScene = new Scene(chatContainer, 500, 300);
        chatDialog.setScene(chatScene);

        nicknameDialog.show();

        //ZMIENIC SCANNER NA DANE Z OKIENEK
        //I WCZYTYWAC NAME Z PIERWSZEGO OKIENKA



        //https://stackoverflow.com/questions/40777560/auto-scroll-in-javafx
        //https://stackoverflow.com/questions/41851501/how-to-design-chatbox-gui-using-javafx/41851855
    }


    public static void main(String[] args) {
        launch(args);

    }
}
