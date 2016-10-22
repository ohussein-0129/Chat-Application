package com.clse.server;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.ArrayList;

/*
 * Author: Osman Hussein
 */
public class HandleClient implements Runnable
{
    private Socket socket = null;
    private byte[] recvBuff;
    private String message;
    private PushbackInputStream in = null;
    private static String clientName = "";
    public HandleClient(Socket s)
    {
        socket = s;
        MainServer ms = new MainServer(0);
        ArrayList<Socket> clients = ms.getClients();
        clients.add(s);
        
        
        String message = "";
        do
        {
            message = receiveMessage();
            sleepThread(80);
        } while(message.length()==0);
        String[] messageArr = message.split(" ");
        clientName = messageArr[1];
    }
    
    public void run()
    {
        Timer timer = new Timer();
        timer.schedule(new HandleMessage(socket, clientName), 0,50);
    }
    
    public String receiveMessage() 
     /*
      * This will only be used when getting the client's name
      *  'HandleMessage' class handles the messages during the chat
      */
    {
        String str = "";
        recvBuff = new byte[1024];
        try
        {
            in = new PushbackInputStream(socket.getInputStream());
            if(in.available()>0)
            { 
                in.read(recvBuff, 0, recvBuff.length);
                str = new String(recvBuff);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        return str;
    }
    
    public void sleepThread(int mills)
    {
        try
        {
            Thread.sleep(mills);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}