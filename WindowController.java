/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Anthony
 */
public class WindowController implements Initializable {

    @FXML
    private Label prompt;
    @FXML
    private Button button;
    @FXML
    private ListView userList;
    @FXML
    private TextArea outputArea;
    @FXML
    private TextField inputArea;

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Integer> array = new ArrayList();
    private int forlist = 0;     
    @FXML private CheckBox cb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        prompt.setText("Enter name: ");
    }

    private void updateList(Vector<Person> list) {
        if(forlist == 0){
            userList.getItems().clear();
            array.clear();
        }
        for (int i = forlist; i < list.size(); ++i) {
            HBox hbox = new HBox(5); 
            cb = new CheckBox();
            hbox.getChildren().addAll(cb,new Label(list.get(i).getName()));
            final int pass = i;
            cb.setOnAction(e->ifCheckBoxClicked(e,pass));    
            userList.getItems().add(hbox);
        }
        forlist = list.size();
        System.out.println("forlist: "+ forlist);
    }

    private void connect() {
        Thread t = new Thread(new Thread() {
            public void run() {
                try {
                    client = new Socket("localhost", 43331);
                    out = new ObjectOutputStream(client.getOutputStream());
                    in = new ObjectInputStream(client.getInputStream());

                    String username = inputArea.getText();
                    out.writeObject(username);

                    prompt.setVisible(false);

                    inputArea.clear();
                    while (true) {
                        Object msg = in.readObject();
                        if (msg instanceof String) {
                            outputArea.appendText((String) msg);
                        } else {
                            System.out.println("new list! " + ((Vector) msg).size());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(((Vector)msg).size() < forlist){
                                        forlist = 0;
                                    }
                                    updateList((Vector<Person>) msg);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void ifKeyPressed(KeyEvent e) {
        // if enter send message/connect
        if (e.getCode().equals(KeyCode.ENTER)) {
            buttonPressed();
        }
    }
    
    @FXML
    private void ifCheckBoxClicked(ActionEvent e, int i){
            for(int a = 0; a < array.size();++a){
                if(array.get(a) == (Object)i){
                    array.remove(a);
                    System.out.println("array.remove aftersize:"+ array.size());
                    return;
                }       
            }
            array.add(i);
            for(int a = 0; a < array.size();++a)
                System.out.println(array.get(a)+ " this is the size "+ array.size());        
    }

    @FXML
    private void buttonPressed() {
        if (client == null) {
            System.out.println("button pressed");
            connect();
            return;
        }
        try {
            String msg = inputArea.getText();
            out.writeObject(msg);
            inputArea.clear();
            System.out.println("arraysize before sending it:"+ array.size());
            out.writeObject(array.size());
            for(int i = 0; i < array.size(); ++i){
                out.writeObject(array.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}