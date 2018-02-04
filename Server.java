/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    private int port = 43331;
    private ServerSocket host;
    private ChatMonitor cm;
    private int countID = 0;

    Server() {
        cm = new ChatMonitor();
        try {
            host = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient() {

        try {
            while (true) {
                Socket newclient = host.accept();
                System.out.println("new user connected!" + (++countID));
                Thread newThread = new Thread(new Thread() {
                    public void run() {
                        ObjectOutputStream out = null;
                        String clientName = null;
                        Person p = null;
                        try {
                            ObjectInputStream in = new ObjectInputStream(newclient.getInputStream());
                            out = new ObjectOutputStream(newclient.getOutputStream());
                            
                            clientName = (String) in.readObject();
                            p = new Person(clientName,countID);
                            cm.addUsers(p, out);
                            // listening for new messages then broadcast to all users
                            while(true){
                                Object msg = in.readObject();
                                ArrayList<Integer> list = new ArrayList<Integer>();
                                int limit = (int)in.readObject();
                                for(int i = 0; i < limit; ++i){
                                    list.add((Integer) in.readObject());
                                }
                                if(msg instanceof String){
                                String clientMsg = (String)msg;
                                cm.broadcast(clientName + ": " + clientMsg  + "\n",list);
                                }
                            }
                        } catch (Exception e) {
                            cm.remove(p, out);
                            e.printStackTrace();
                        }
                    }
                });
                newThread.setDaemon(true);
                newThread.start();
            }
        } catch (Exception e) {            
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.handleClient();
    }

}
