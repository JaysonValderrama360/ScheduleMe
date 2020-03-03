/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Main Windows Controller
 * 
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class MainWindowController implements Initializable{
    
    @FXML
    Button customersBtn;
    @FXML
    Button appointmentsBtn;
    @FXML
    Button calendarBtn;
    @FXML
    Button reportsBtn;
    @FXML
    Button logFileBtn;
    @FXML
    Label userNameLabel;
    private String userName;
    private int userId;
    
    
    // to make this better I would create an interface to implement getUserName in all my
    // controllers and turn the window load into a function to avoid code repeating
    @FXML
    void CustomersBtnClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customers.fxml"));
            Parent root = loader.load();
            CustomersController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Customers");
            stage.setScene(new Scene(root));
            controller.getUserName(userName);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load window");
            alert.setContentText("Sorry, the window has failed to load");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    void AppointmentsBtnClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Appointments.fxml"));
            Parent root = loader.load();
            AppointmentsController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Appointments");
            stage.setScene(new Scene(root));
            controller.getUserName(userName, userId);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load window");
            alert.setContentText("Sorry, the window has failed to load");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    void CalendarBtnClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
            Parent root = loader.load();
            CalendarController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Calendar");
            stage.setScene(new Scene(root));
            controller.getUserName(userName, userId);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load window");
            alert.setContentText("Sorry, the window has failed to load");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    void ReportsBtnClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reports.fxml"));
            Parent root = loader.load();
            ReportsController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root));
            controller.getUserName(userName, userId);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load window");
            alert.setContentText("Sorry, the window has failed to load");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    void LogFileBtnClick (ActionEvent event) {
        try {
            // Open text editor with log file, will fail on anything before windows XP
            Desktop.getDesktop().edit(new File("LoginRecord.txt"));
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open text file");
            alert.setContentText("Sorry the text editor failed to open");
            alert.showAndWait();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
   
    
    public void getUserName(String uName, int uId) {
        userName = uName;
        userId = uId;
        userNameLabel.setText("User: " + userName);
    }
}
