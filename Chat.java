/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Anthony
 */
public class Chat extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader l = new FXMLLoader(getClass().getResource("Window.fxml"));
        Parent root = (Parent)l.load();
       // WIndowController c = (WIndowController) l.getController();
        //c.setStage(stage);
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}