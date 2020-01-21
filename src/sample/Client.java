package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client extends Application {
    final int width = 800, height = 500;
    String sender, receiver;
    SendMessage sendMessage;
    ReadMessage readMessage;
    static VBox messagesContainer;


    void initializeCommunication(){
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 59090);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage = new SendMessage(socket, sender);
        readMessage = new ReadMessage(socket);
        Thread sendMessageThread = new Thread(sendMessage);
        Thread readMessageThread = new Thread(readMessage);

        readMessageThread.start();
        sendMessageThread.start();
    }

    List<String> getUserList(){
        List<String> userList = new ArrayList<>();

        if(sendMessage != null){
            String list = readMessage.msg.msg;
            String[] str = list.split(",");
            sendMessage.send(sender, receiver, "init");
            userList = Arrays.asList(str);
        }
        return userList;
    }

    static void showMessage(Message msg){
        messagesContainer.getChildren().add(new Label(msg.msg));
    }

    @Override
    public void start(Stage primaryStage){
        Stage senderDialog = new Stage();
        Stage userDialog = new Stage();
        Stage chatDialog = new Stage();

        Button senderBtn = new Button("Ok");
        Text entersender = new Text(50, 50, "Enter your nickname");
        entersender.setFont(Font.font(20));
        TextField senderField = new TextField();

        senderBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sender = senderField.getText();
                senderDialog.close();
                initializeCommunication();
                sendMessage.send(sender, "server", "init");
                userDialog.show();
            }
        });

        VBox senderVBox = new VBox();
        senderVBox.getChildren().addAll(entersender, senderField, senderBtn);
        Scene senderScene = new Scene(senderVBox, width, height);
        senderDialog.setScene(senderScene);

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
                sendMessage.send(sender, "server", "init");
                userList.setItems(FXCollections.observableList(getUserList()));
            }
        });

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                receiver = userList.getSelectionModel().getSelectedItem();
                userDialog.close();
                chatDialog.show();
            }
        });

        BorderPane chatContainer = new BorderPane();
        messagesContainer = new VBox();
        TextField messageField = new TextField();

        List<String> list = new ArrayList<>();
        ObservableList<String> observableList = FXCollections.observableList(list);
        observableList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                messagesContainer.getChildren().add(new Label(change.getList().get(list.size()-1)));
            }
        });

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!messageField.getText().isEmpty()){
                    sendMessage.send(sender, receiver, messageField.getText());
                    observableList.add(messageField.getText());
                    messageField.setText("");
                }
            }
        });

        HBox bottomContainer = new HBox();
        bottomContainer.setAlignment(Pos.BOTTOM_CENTER);

        bottomContainer.getChildren().addAll(messageField, sendBtn);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        chatContainer.setTop(messagesContainer);
        chatContainer.setBottom(bottomContainer);

        Scene chatScene = new Scene(chatContainer, 500, 300);
        chatDialog.setScene(chatScene);
        senderDialog.show();
        //https://stackoverflow.com/questions/40777560/auto-scroll-in-javafx
        //https://stackoverflow.com/questions/41851501/how-to-design-chatbox-gui-using-javafx/41851855
    }


    public static void main(String[] args) {
        launch(args);
    }
}
