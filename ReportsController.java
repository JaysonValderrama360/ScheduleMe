/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import Models.Appointment;
import Models.User;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import util.DBConnection;
import util.TimeConverter;
import util.UserConverter;

/**
 * FXML Controller class
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class ReportsController implements Initializable {
    
    String userName;
    int userId;
    @FXML
    private Label userNameLabel;
    @FXML
    private ComboBox apptTypeMonthComboBox;
    @FXML
    private ListView apptTypeListView;
    @FXML
    private ComboBox consultantMonthBox;
    @FXML
    private ComboBox consultantBox;
    @FXML
    private Button generateConsultantBtn;
    @FXML
    private ListView consultantSchedList;
    @FXML
    private Button generateLocationBtn;
    @FXML
    private ComboBox locationMonthBox;
    @FXML
    private ComboBox locationBox;
    @FXML
    private ListView locationListBox;
    private HashMap<String, Integer> typeMap = new HashMap<>();
    private ArrayList<String> months = new ArrayList<>();
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        populateMonthBox(apptTypeMonthComboBox);
        populateMonthBox(consultantMonthBox);
        populateMonthBox(locationMonthBox);
        populateConsultantBox();
        populateLocationBox();
        // using a lambda here to assign the action event for the button click
        // this can be done in place of using scene building and is easier
        // than the old way to set action events. if needed I could code the entire
        // function in this lambda.
        generateLocationBtn.setOnAction(event -> generateLocationReport());
    }
    
    public void generateLocationReport() {
        ArrayList<Appointment> appts = new ArrayList<>();
        String location = (String) locationBox.getSelectionModel().getSelectedItem();
        String month = (String) locationMonthBox.getSelectionModel().getSelectedItem();
        if (location != null && month != null) {
            try {
                DBConnection.connect();
                ResultSet rs = DBConnection.query("*", "appointment", "location = '" + location + 
                        "' AND MONTHNAME(start) = '" + month + "'");
                while (rs.next()) {
                    Appointment appt = DBConnection.getAppointment(rs);
                    appts.add(appt);
                }
                locationListBox.setItems(FXCollections.observableList(appts));
            } catch (SQLException e) {
                System.out.println("Failed to get appointments");
            } finally {
                DBConnection.closeConn();
            }
        }
    }
    
    public void populateLocationBox() {
        ArrayList<String> locations = new ArrayList<>();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "appointment");
            while (rs.next()) {
                if (!locations.contains(rs.getString("location"))) {
                    locations.add(rs.getString("location"));
                }
            }
            locationBox.setItems(FXCollections.observableList(locations));
        } catch (SQLException e) {
            System.out.println("Failed to retrieve appointment list");
        } finally {
            DBConnection.closeConn();
        }
    }
    
    @FXML
    public void generateConsultantReport() {
        User consultant = (User) consultantBox.getSelectionModel().getSelectedItem();
        String month = (String) consultantMonthBox.getSelectionModel().getSelectedItem();
        ArrayList<Appointment> appts = new ArrayList<>();
        if (consultant != null && month != null) {
            try {
                DBConnection.connect();
                ResultSet rs = DBConnection.query("*", "appointment", "userId = " + consultant.getUserID() +
                        " AND MONTHNAME(start) = '" + month + "'");
                while (rs.next()) {
                    Appointment appt = DBConnection.getAppointment(rs);
                    appts.add(appt);
                }
                consultantSchedList.setItems(FXCollections.observableList(appts));
            } catch (SQLException e) {
                System.out.println("Failed to get appt list");
            } finally {
                DBConnection.closeConn();
            }
        }
    }
    
    public void populateConsultantBox() {
        ArrayList<User> consultants = new ArrayList<>();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "user");
            while (rs.next()) {
                User user = new User(rs.getInt("userId"), rs.getString("userName"));
                consultants.add(user);
            }
            consultantBox.setConverter(new UserConverter());
            consultantBox.setItems(FXCollections.observableList(consultants));
        } catch (SQLException e) {
            System.out.println("Failed to get user list");
        } finally {
            DBConnection.closeConn();
        }
    }
    
    public void populateMonthBox(ComboBox box) {
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        box.setItems(FXCollections.observableList(months));
    }
    
    @FXML
    public void typeMonthSelect() {
        typeMap.clear();
        String month = (String) apptTypeMonthComboBox.getSelectionModel().getSelectedItem();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "appointment", "MONTHNAME(start) = '" + month +  "'");
            while (rs.next()) {
                Integer oldValue = typeMap.get(rs.getString("type"));
                if (oldValue != null) {
                    Integer newValue = oldValue + 1;
                    typeMap.replace(rs.getString("type"), newValue);
                } else {
                    typeMap.put(rs.getString("type"), 1);
                }
            }
            ArrayList<String> types = new ArrayList<>();
            
            // using a lambda here to replace a for each statement on the hashmap.
            // this is a more efficient way to write this code.
            typeMap.entrySet().forEach((entry) -> {
                types.add("Type: " + entry.getKey() + " Number: " + entry.getValue());
            });
            apptTypeListView.setItems(FXCollections.observableList(types));
            
        } catch (SQLException e ) {
            System.out.println("failed to get appointments");
        } finally {
            DBConnection.closeConn();
        }
    }
    
    public void getUserName(String uName, int uId) {
        userName = uName;
        userId = uId;
        userNameLabel.setText("User: " + userName);
    }
    
}
