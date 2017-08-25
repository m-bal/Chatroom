/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Anthony
 */
public class ChatMonitor {

    Vector<ObjectOutputStream> users = new Vector<ObjectOutputStream>();
    Vector<Person> userNames = new Vector<Person>();
    synchronized public void addUsers(Person p, ObjectOutputStream s) {
        userNames.add(p);
        users.add(s);
        broadcast(new Vector<Person>(userNames), new ArrayList());
        broadcast(p.getName() + " connected!\n", new ArrayList());
    }
    synchronized public void remove(Person p, ObjectOutputStream s){
        userNames.remove(p);
        users.remove(s);
        broadcast(new Vector<Person>(userNames), new ArrayList());
        
        broadcast(p.getName() + " Disconnected!\n", new ArrayList());
    }

    synchronized public void broadcast(Object msg, ArrayList<Integer> list) {
        try {
            System.out.println(list.size()+" this is list");
            if(list.size() == 0){
            for (int i = 0; i < users.size(); ++i) {
                users.get(i).writeObject(msg);
            }
            }else{
                for(int i = 0; i < list.size(); ++i){
                    users.get((int) list.get(i)).writeObject(msg);
                }
            }
            System.out.println("this is users.size:"+users.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}