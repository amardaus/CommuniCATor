package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client extends Application {
    final int width = 800, height = 500;
    String nickname;
    SendMessage sendMessage;
    ReadMessage readMessage;

    void initializeCommunication(){
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 59090);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage = new SendMessage(socket, nickname);
        readMessage = new ReadMessage(socket);
        Thread sendMessageThread = new Thread(sendMessage);
        Thread readMessageThread = new Thread(readMessage);

        readMessageThread.start();
        sendMessageThread.start();
    }

    List<String> getUserList(){
        List<String> userList = new ArrayList<>();

        if(sendMessage != null){
            sendMessage.setLine("init");
            sendMessage.setReceiver("server");

            String list = readMessage.msg.msg;
            System.out.println(list);
            String[] str = list.split(" ");
            userList = Arrays.asList(str);
        }
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
                initializeCommunication();
            }
        });

        VBox nicknameVBox = new VBox();
        nicknameVBox.getChildren().addAll(enterNickname, nicknameField, nicknameBtn);
        Scene nicknameScene = new Scene(nicknameVBox, width, height);
        nicknameDialog.setScene(nicknameScene);

        VBox userListBox = new VBox();
        Scene userListDialog = new Scene(userListBox, width, height);
        userDialog.setScene(userListDialog);
        Button refreshBtn = new Button("Refresh");

        ListView<String> userList = new ListView<>();
        ObservableList<String> users = FXCollections.observableArrayList(getUserList());
        userList.setItems(users);
        userListBox.getChildren().addAll(userList, refreshBtn);

        refreshBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userList.setItems(FXCollections.observableList(getUserList()));
            }
        });

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userDialog.close();
                chatDialog.show();
            }
        });

        BorderPane chatContainer = new BorderPane();
        VBox messagesContainer = new VBox();
        TextField messageField = new TextField();

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!messageField.getText().isEmpty()){
                    sendMessage.setLine(messageField.getText());
                    sendMessage.setReceiver("B");
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


        //https://stackoverflow.com/questions/40777560/auto-scroll-in-javafx
        //https://stackoverflow.com/questions/41851501/how-to-design-chatbox-gui-using-javafx/41851855
    }


    public static void main(String[] args) {
        launch(args);

    }
}
