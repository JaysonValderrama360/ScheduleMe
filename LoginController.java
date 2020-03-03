/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DBConnection;
import util.TimeConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class LoginController implements Initializable {
    
    @FXML
    private Label nameLbl;
    @FXML
    private Label passLbl;
    @FXML
    private TextField nameBox;
    @FXML
    private TextField passBox;
    @FXML
    private Button loginBtn;
    @FXML
    private Label errorLbl;
    @FXML
    private Label welcomeLbl;
    private int userId;
    
    ResourceBundle rb;
    
    @FXML
    private void LoginButtonHandler(ActionEvent event) {
        String userName = nameBox.getText();
        String password = passBox.getText();
        if (loginCheck(userName, password)) {
            logWriter(userName);
            upcomingApptCheck(userName);
            // change scene to the main program window
            // if any error occurs loading the window print error info to stdout
            // throw error message so user isn't confused about nothing happening
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
                Parent root = loader.load();
                MainWindowController controller = loader.getController();
                Scene mainWindowScene = new Scene(root);
                Stage mainWindow = (Stage) ((Node)event.getSource()).getScene().getWindow();
                mainWindow.setTitle("Scheduler");
                mainWindow.setScene(mainWindowScene);
                controller.getUserName(userName, userId);
                mainWindow.show();
            } catch (IOException e) {
                System.out.println("Failed to open main window: " + e.getStackTrace());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Problem loading main window");
                alert.setContentText("Sorry, the main window has failed to load");
                alert.showAndWait();
            }
        }
        else {
            errorLbl.setText(this.rb.getString("badpass"));
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        System.out.println(Locale.getDefault());
        Locale.getDefault();
        loginBtn.setText(rb.getString("login"));
        nameLbl.setText(rb.getString("username"));
        passLbl.setText(rb.getString("password"));
        welcomeLbl.setText(rb.getString("welcome"));
    }    
    
    private boolean loginCheck(String userName, String Password){
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "user", "userName='"+userName +"'");
            while (rs.next()) {
                if (rs.getString("password").equals(Password)) {
                    userId = rs.getInt("userId");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("database connection failed");
        } finally {
            DBConnection.closeConn();
        }
        return false;
    }
    
    private void upcomingApptCheck(String userName) {
        // Check for appointment within 15 minutes and alert user if one exists
            try {
                DBConnection.connect();
                ResultSet user = DBConnection.query("*", "user", "userName='" + userName + "'");
                user.next();
                int uId = user.getInt("userId");
                ResultSet rs = DBConnection.query("*", "appointment", "userId=" + uId);
                ZonedDateTime currentTime = ZonedDateTime.now();
                int year = currentTime.getYear();
                int day = currentTime.getDayOfYear();
                int month = currentTime.getMonthValue();
                int time = (currentTime.getHour() * 60) + (currentTime.getMinute());
                while (rs.next()) {
                    ZonedDateTime appt = TimeConverter.getLocalTime(rs.getString("start"));
                    int apptYear = appt.getYear();
                    int apptDay = appt.getDayOfYear();
                    int apptMonth = appt.getMonthValue();
                    int apptTime = (appt.getHour() * 60) + (appt.getMinute());
                    if (year == apptYear && day == apptDay && month == apptMonth
                            && (apptTime - time) > -1 && (apptTime - time) < 16) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Upcoming Appointment");
                        alert.setHeaderText("Appointment starting within next 15 minutes");
                        alert.setContentText("You have an appointment starting within the next 15 minutes");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {

            } finally {
                DBConnection.closeConn();
            }
    }
    
    private void logWriter(String userName) {
        // clear any error messages on the window and then write the login time
        // to the log file, catch any IO issues with writing to the file and 
        // print the stack trace to stdout
        try {
                errorLbl.setText("");
                System.out.println("success");
                BufferedWriter writer = new BufferedWriter(new FileWriter("LoginRecord.txt", true));
                writer.newLine();
                writer.write("Username: " + userName + " Time: " + LocalDateTime.now());
                writer.close();
            } catch (IOException e) {
                System.out.println("unable to write to file :" + e.getStackTrace() );
            }
    }
}
