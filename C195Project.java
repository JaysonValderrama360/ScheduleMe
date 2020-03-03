/*
 *  Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class C195Project extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
        
        Parent main = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setResources(rb);
            main = loader.load();
            
            Scene scene = new Scene(main);
            
            stage.setScene(scene);
            stage.setTitle("Login");
            
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
