package com.clse.client;

import java.net.Socket;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Timer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ContentDisplay;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

/*
 * Author: Osman Hussein
 */
public class Client extends Application 
{
    private Socket socket = null;
    public static final String server_ip = "127.0.0.1"; //server ip address change if you wish
    public static final int port_num = 1024; //port number
    private DataOutputStream os = null;
    private byte[] sendBuff;
    private static String serverAddress;
    private Button sendBtn;
    private static Label display;
    private Timer timer;
    private ReceiveClass rc;
    private static String name;
    private Stage window;
    private Scene scene;
    
    
    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("Chat");
        window.setResizable(false);
        
        int height = 500, width = 350;
        
        window.setOnCloseRequest(e -> closeOperations()); 
        
        display = new Label();
        display.setStyle(textBoxStyle());
        display.setPrefWidth(width-25);
        display.setMinHeight(height-50);
        display.setContentDisplay(ContentDisplay.TOP);
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(display);
        sp.setPrefSize(width, height-50);
        HBox hor = new HBox(12);
        
        VBox ver = new VBox(10);
        ver.setPadding(new Insets(5, 10, 5, 10));
        hor.setPadding(new Insets(5, 3, 5, 5));
        
        TextField entry = new TextField();
        entry.setPrefWidth(200);
        entry.setText("Type here...");
        entry.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    if(entry.getText().toString().trim().length()>0)
                    {
                        sendMessage(entry.getText().toString());
                        entry.setText("");
                    }
                    entry.setText("");
                }
            }
        });
        
        sendBtn = new Button();
        sendBtn.setText("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(entry.getText().toString().trim().length()>0)
                {
                    sendMessage(entry.getText().toString());
                    entry.setText("");
                }
                entry.setText("");
            }
        });
        
        //or if using java 8 -- btn.setOnAction(e-> /*do something*/);
        
        hor.getChildren().addAll(entry, sendBtn);
        hor.setAlignment(Pos.BOTTOM_CENTER);
        ver.getChildren().addAll(sp);
        
        BorderPane bp = new BorderPane(); 
        bp.setTop(ver);
        bp.setBottom(hor);
        scene = new Scene(bp, width, height);
        window.setScene(scene);
        
        window.show();
        runClient();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
    public String textBoxStyle()
    {
        String str = "-fx-background-color: white;\n";
        str+= "-fx-border-color: black;\n";
        str+= "-fx-padding: 10 10 10 10;";
        return str;
    }
     
    public void runClient()
    {
        try{
            System.out.println("Finding Server");
            if(socket==null)
                  socket = new Socket(server_ip, port_num);
            System.out.println("Connected to " +socket.getInetAddress());
            name = NameAlert.getClientNameWin();
            sendMessage("Name: " +name);
            //so we can use the same object for the timer rather than re-creating it
            rc = new ReceiveClass(socket, display); 
            
            timer = new Timer();
            timer.schedule(rc, 0,300);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void sendMessage(String message) 
    {
        try
        {
            sendBuff = new byte[2048];
            os = new DataOutputStream(socket.getOutputStream());
            sendBuff = message.getBytes();
            os.write(sendBuff, 0, sendBuff.length); //sending message...
            System.out.println("Message Sent");
            os.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void closeThings()
    {
        if(socket!=null)
        {
            try
            {
                socket.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        
        if(os!=null)
        {
            try
            {
                os.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        timer.cancel();
        //stops timer
        window.close();
    }
    
    public void closeOperations()
    {
        closeThings();
        window.close();
    }
}