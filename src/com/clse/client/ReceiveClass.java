package com.clse.client;

import java.util.TimerTask;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.*;

/**
 * Author: Osman Hussein
 * 
 * This class will check for any messages that could be read
 */
public class ReceiveClass extends TimerTask
{
    private Socket socket = null;
    private byte[] buff;
    private static String message;
    private Label display;
    private static String displayText;
    private PushbackInputStream in = null;
    public ReceiveClass(Socket s, Label d)
    {
        socket = s;
        message = "";
        displayText = "";
        display = d;
    }
    
    public void run()
    {
        try
        {
            if(socket!=null)
            {
                buff = new byte[2048];
                PushbackInputStream in = new PushbackInputStream(socket.getInputStream());
                if(in.available()>0)
                {
                    int length = in.read(buff, 0, buff.length);
                    
                    //basically running a thread in a thread as JavaFX is a thread based UI
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            if(length!=1020)
                            {
                                //so that an empty line does not print onto console
                                displayText+=new String(buff);
                                displayText+="\n";
                                display.setText(new String(displayText));
                            } 
                        }
                    });
                    message = "";
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}