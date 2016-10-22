package com.clse.server;

import java.util.TimerTask;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.DataOutputStream;
import java.net.*;

/*
 * Author: Osman Hussein
 */
public class HandleMessage extends TimerTask
{
    private Socket socket = null;
    private byte[] sendBuff;
    private byte[] recvBuff;
    private String message;
    private PushbackInputStream in = null;
    private DataOutputStream os = null;
    private ArrayList<Socket> clients;
    private ArrayList<Socket> temp;
    private String clientName = null;
    private static boolean remove = false;
    private MainServer ms;
    
    public HandleMessage(Socket s, String name)
    {
        socket = s;
        clientName = name;
        message = "";
    }
    
    public void run()
    {
        ms = new MainServer(0);
        clients = ms.getClients();
        try
        {
            message = receiveMessage();
            
            if(message.length()>0)
            {
                broadcast(clientName +": " +message);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void broadcast(String message) throws IOException
      /*
       * sends the message to all of the clients (the one received earlier)
       * before the text the user has typed is displayed, it has to go through the server first.
       */
    {
        int i;
        temp = new ArrayList<Socket>();
        sendBuff = new byte[2048];
        sendBuff = message.getBytes();
        for(Socket cl : clients)
        {
            try
            {
                os = new DataOutputStream(cl.getOutputStream());
                os.write(sendBuff, 0, sendBuff.length);
                os.flush();
            }
            catch(SocketException e)
               /*
                * If this is caught then a client has disconnected
                */
            {
                temp.add(cl); //add the socket object which will be removed
                if(remove==false)
                {
                    remove = true;
                }
            }
        }
        
        if(remove==true)
        {
            //remove all disconnected clients
            for(Socket t : temp)
            {
                clients.remove(t);
            }
            ms.setClientsArray(clients);
            remove = false;
        }
    }
    
    public String receiveMessage() throws IOException
         /*
          * Receive message from client
          */
    {
        String str = "";
        recvBuff = new byte[2048];
        in = new PushbackInputStream(socket.getInputStream());
        if(in.available()>0)
        {
            in.read(recvBuff, 0, recvBuff.length);
            str = new String(recvBuff);
        }
        return str;
    }
}