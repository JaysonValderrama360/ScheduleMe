/*
 * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import Models.Appointment;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import util.DBConnection;

/**
 * FXML Controller class
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class CalendarController implements Initializable {
    private String userName;
    private int userId;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView weekListView;
    @FXML
    private ListView monthListView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setWeekly();
        setMonthly();
    }
    
    public void setWeekly() {
        try {
            DBConnection.connect();
            ResultSet rs =DBConnection.query("*", "appointment", "YEARWEEK(start,1) = YEARWEEK(CURDATE(), 1)");
            ArrayList<Appointment> apptList = new ArrayList<>();
            while (rs.next()) {
                Appointment appt =DBConnection.getAppointment(rs);
                apptList.add(appt);
            }
            weekListView.setItems(FXCollections.observableList(apptList));
        } catch (SQLException e) {
            System.out.println("unable to get appointment list");
        } finally {
            DBConnection.closeConn();
        }
    }
    
    public void setMonthly() {
        try {
            DBConnection.connect();
            ResultSet rs =DBConnection.query("*", "appointment", "YEAR(start) = YEAR(CURDATE()) AND"
                    + " MONTH(start) = MONTH(CURDATE())");
            ArrayList<Appointment> apptList = new ArrayList<>();
            while (rs.next()) {
                Appointment appt =DBConnection.getAppointment(rs);
                apptList.add(appt);
            }
            monthListView.setItems(FXCollections.observableList(apptList));
        } catch (SQLException e) {
            System.out.println("unable to get appointment list");
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
