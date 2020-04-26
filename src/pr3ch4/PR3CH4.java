/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr3ch4;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author MOMEN
 */
public class PR3CH4 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
       
        
        
        
         Pane paneView = FXMLLoader.load(getClass().getResource("views.fxml"));
        Scene scene = new Scene(paneView, 800, 700);
        
        primaryStage.setTitle("IUG-Student Srvice");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
