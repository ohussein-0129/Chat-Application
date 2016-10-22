package com.clse.client;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * This class will display window that will ask for a name
 */
public class NameAlert
{
    private static String name;
    public static String getClientNameWin()
    {
        Stage nameWindow = new Stage();
        nameWindow.initModality(Modality.APPLICATION_MODAL);
        nameWindow.setTitle("Name?");
        
        nameWindow.setOnCloseRequest(e -> e.consume()); 
        
        Label info = new Label();
        info.setText("Enter Your name: ");
        
        TextField nameEntry = new TextField();
        
        Button subBtn = new Button("Submit");
        subBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                 if(nameEntry.getText().toString().trim().length()>0)
                 {
                     name = nameEntry.getText().toString();
                     nameWindow.close();
                 }
            }
        });
        
        HBox btn_tx = new HBox(5);
        btn_tx.getChildren().addAll(nameEntry, subBtn);
        btn_tx.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(info, btn_tx);
        layout.setPrefSize(210,70);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        nameWindow.setScene(scene);
        nameWindow.showAndWait();
        return name;
    }
}