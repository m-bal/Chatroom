/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app;

import java.io.Serializable;

/**
 *
 * @author manvir
 */
public class Person implements Serializable{
    private String name;
    private int ID;
    
    Person(String na, int id){
        ID = id;
        name = na;
    }
    String getName(){                
        return name;
    }
    int getID(){
        return ID;
    }
}
