package com.clse.server;

import java.net.*;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Author: Osman Hussein
 * 
 * The server handles the messages while the clients chat to each other
 */
public class MainServer
{
    private ServerSocket serverSocket = null;
    private static ArrayList<Socket> clients;
    private static final String ip_addr = "127.0.0.1";//use your address if you want
    private static int port_n = 1024;
    private Thread t1, t2, t3, t4, t5; //up to 5 clients
    
    
    public MainServer()
    {
        try
        {
            //should accept 5 connections max (still counts even if they disconnected)
            clients = new ArrayList<Socket>();
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip_addr, port_n));
            System.out.println("Finding Connection...");
            t1 = new Thread(new HandleClient(serverSocket.accept()));
            t1.start();
            t2 = new Thread(new HandleClient(serverSocket.accept()));
            t2.start();
            t3 = new Thread(new HandleClient(serverSocket.accept()));
            t3.start();
            t4 = new Thread(new HandleClient(serverSocket.accept()));
            t4.start();
            t5 = new Thread(new HandleClient(serverSocket.accept()));
            t5.start();
            joinThread(t1);
            joinThread(t2);
            joinThread(t3);
            joinThread(t4);
            joinThread(t5);
            serverSocket.close();
        }
        catch(IOException io)
        {
            System.err.println("Cannot create socket");
            io.printStackTrace();
        }
        finally
        {
            if(serverSocket!=null)
            {
                try
                {
                    serverSocket.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public MainServer(int a){} //contructor just for access
    
    public boolean joinThread(Thread t)
    {
        boolean res = false;
        try
        {
            t.join();
            res = true;
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public void setClientsArray(ArrayList<Socket> cl)
    {
        this.clients  = cl;
    }
    public ArrayList<Socket> getClients()
    {
        return this.clients;
    }
    
    public static void main(String[] args)
    {
        MainServer ms = new MainServer();
    }
}