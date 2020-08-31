package httpserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ruben.hume
 */
public class HttpReceiver extends Thread {
    private static ThreadPool threadPool;
    private ServerSocket srvrSock;
    private boolean running = true;
    public HttpReceiver(ThreadPool tp, int port) {
        threadPool = tp;
        Gui.msgToConsole("INFO", "Starting server on port " + port + "...");
        // Starts the server on port 80
        try {
            srvrSock = new ServerSocket(port);
        } catch(IOException ex) {
            Gui.msgToConsole("ERROR", ex.toString());
        }
        
    }
    
    @Override
    public void run() {
        try{
            Socket clientSock;
            Gui.msgToConsole("INFO", "Listening for incoming connections...");
            // Listens for incoming requests
            while(running == true) {
                clientSock = srvrSock.accept();
                // If a request is detected, a thread from the threadpool takes care of it
                if(clientSock.isConnected()) {
                    threadPool.addTask(new HttpHandler(clientSock));
                }
            }
        } catch(Exception ex) {
            Gui.msgToConsole("ERROR", ex.toString());
        }
    }
    
    public void terminate() {
        Gui.msgToConsole("INFO", "Stopping server...");
        try {
            srvrSock.close();
        } catch(IOException ex) {
            Gui.msgToConsole("ERROR", ex.toString());
        }
        running = false;
    }
}
