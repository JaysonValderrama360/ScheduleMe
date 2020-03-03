/*
 *  Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import Models.Appointment;
import Models.Customer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.CustomerConverter;
import util.DBConnection;
import util.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class AppointmentsController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private ComboBox customerComboBox;
    @FXML
    private TextField titleTxtBox;
    @FXML
    private TextField descriptionTxtBox;
    @FXML
    private TextField locationTxtBox;
    @FXML
    private TextField contactTxtBox;
    @FXML
    private TextField typeTxtBox;
    @FXML
    private TextField urlTxtBox;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private ChoiceBox startTimeChoiceBox;
    @FXML
    private ChoiceBox endTimeChoiceBox;
    @FXML
    private Button editButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button deleteBtn;
    @FXML
    private TableView apptTableView;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> dateTableColumn;
    @FXML
    private TableColumn<Appointment, String> typeTableColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionTableColumn;
    private String userName;
    private ArrayList<Appointment> apptList = new ArrayList<>();
    private ObservableList<Appointment> obsApptList= FXCollections.observableList(apptList);
    private boolean edit = false;
    private int userId;
    private int apptId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userNameLabel.setText("User: " + userName);
        populateTimes();
        populateCustomers();
    }    
    
    @FXML
    public void customerSelect() {
        Customer cust = (Customer) customerComboBox.getSelectionModel().getSelectedItem();
        populateApptTable(cust.getCustomerID());
    }
    
    @FXML
    public void editAppt() {
        if (edit == false) {
            if (apptTableView.getSelectionModel().getSelectedItem() != null) {
                Appointment appt = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
                titleTxtBox.setText(appt.getTitle());
                descriptionTxtBox.setText(appt.getDescription());
                locationTxtBox.setText(appt.getLocation());
                contactTxtBox.setText(appt.getContact());
                typeTxtBox.setText(appt.getType());
                urlTxtBox.setText(appt.getURL());
                customerComboBox.setDisable(true);
                dateDatePicker.setValue(LocalDate.of(appt.getStart().getYear(), appt.getStart().getMonth(), appt.getStart().getDayOfMonth()));
                int startHour = appt.getStart().getHour();
                String startMinute = Integer.toString(appt.getStart().getMinute());
                int endHour = appt.getEnd().getHour();
                String endMinute = Integer.toString(appt.getEnd().getMinute());
                if (startMinute.equals("0")) {
                    startMinute = "00";
                }
                if (endMinute.equals("0")) {
                    endMinute = "00";
                }
                if (startHour > 12) {
                    startHour -= 12;
                    startTimeChoiceBox.setValue(startHour + ":" + startMinute + " PM");
                } else if (startHour == 12) {
                    startTimeChoiceBox.setValue(startHour + ":" + startMinute + " PM");
                } else {
                    startTimeChoiceBox.setValue(startHour + ":" + startMinute + " AM");
                }
                if (endHour > 12) {
                    endHour -= 12;
                    endTimeChoiceBox.setValue(endHour + ":" + endMinute + " PM");
                } else if (endHour == 12) {
                    endTimeChoiceBox.setValue(endHour + ":" + endMinute + " PM");
                } else {
                    endTimeChoiceBox.setValue(endHour + ":" + endMinute + " AM");
                }
                edit = true;
                editButton.setText("Cancel");
                apptId = appt.getAppointmentID();
            }
        } else {
            stopEdit();
        }
    }
    
    public void stopEdit() {
        edit = false;
        clearFields();
    }
    
    public void clearFields() {
        titleTxtBox.clear();
        descriptionTxtBox.clear();
        locationTxtBox.clear();
        contactTxtBox.clear();
        typeTxtBox.clear();
        urlTxtBox.clear();
        dateDatePicker.setValue(null);
        startTimeChoiceBox.setValue(null);
        endTimeChoiceBox.setValue(null);
        apptId = 0;
        editButton.setText("Edit");
        customerComboBox.setDisable(false);
    }
    
    @FXML
    public void submitAppt() {
        Appointment appt = checkFields();
        if (appt != null) {
            if (edit == true) {
                if (!TimeConverter.conflictCheck(userId, appt.getStart(), appt.getEnd(), apptId)) {
                    appt.setAppointmentID(apptId);
                    String apptValues = "title = \"" + appt.getTitle() + "\", description = \"" +
                            appt.getDescription() + "\", location = \"" + appt.getLocation() +
                            "\", contact = \"" + appt.getContact() + "\", type = \"" + appt.getType() +
                            "\", url = \"" + appt.getURL() + "\", start = \"" + TimeConverter.getDateTimeString(appt.getStart()) +
                            "\", end = \"" + TimeConverter.getDateTimeString(appt.getEnd()) + "\", lastUpdate = CURRENT_TIMESTAMP, "
                            + "lastUpdateBy = \"" + userName + "\"";
                    try {
                        DBConnection.connect();
                        DBConnection.update("appointment", apptValues, "appointmentId = " + appt.getAppointmentID());
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Updated");
                        alert.setHeaderText("Update Complete");
                        alert.setContentText("The appointment has been edited");
                        alert.showAndWait();
                        stopEdit();
                    } catch (SQLException e) {
                        System.out.println("problem with connection");
                        e.printStackTrace();
                    } finally {
                        DBConnection.closeConn();
                        customerSelect();
                    }
                } else {
                    // throw alert here
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Time Conflict");
                    alert.setHeaderText("There is a problem with the times selected");
                    alert.setContentText("Please make sure that the start time is before the end time"
                            + "and that the appointment does not overlap with other appointments.");
                    alert.showAndWait();
                }
            } else {
                if (!TimeConverter.conflictCheck(userId, appt.getStart(), appt.getEnd(), apptId)) {
                    String apptValues = "(" +appt.getCustomerID() + ", " + appt.getUserID() + ", '"
                            + appt.getTitle() + "', '" + appt.getDescription() + "', '" + appt.getLocation() +
                            "', '" + appt.getContact() + "', '" + appt.getType() + "', '" + appt.getURL() +
                            "', '" + TimeConverter.getDateTimeString(appt.getStart()) + "', '" + TimeConverter.getDateTimeString(appt.getEnd())
                            + "', CURRENT_TIMESTAMP, '" + userName + "', CURRENT_TIMESTAMP, '" + userName +"')";
                    try {
                        DBConnection.connect();
                        DBConnection.insert("appointment (customerId, userId, title, description, "
                                + "location, contact, type, url, start, end, createDate, createdBy, "
                                + "lastUpdate, lastUpdateBy) ", apptValues);
                        clearFields();
                    } catch (SQLException e) {
                        System.out.println("problem with connection");
                        e.printStackTrace();
                    } finally {
                        DBConnection.closeConn();
                        customerSelect();
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Time Conflict");
                    alert.setHeaderText("There is a problem with the times selected");
                    alert.setContentText("Please make sure that the start time is before the end time"
                            + "and that the appointment does not overlap with other appointments.");
                    alert.showAndWait();
                }
            }
        }
    }
    
    public Appointment checkFields() {
        if (customerComboBox.getSelectionModel().getSelectedItem() == null) {
            return null;
        }
        Customer cust = (Customer) customerComboBox.getSelectionModel().getSelectedItem();
        int custId = cust.getCustomerID();
        String title = titleTxtBox.getText();
        String desc = descriptionTxtBox.getText();
        String loc = locationTxtBox.getText();
        String contact = contactTxtBox.getText();
        String type = typeTxtBox.getText();
        String url = urlTxtBox.getText();
        LocalDate date = dateDatePicker.getValue();
        String start = (String) startTimeChoiceBox.getValue();
        String end = (String) endTimeChoiceBox.getValue();
        
        if (date != null && start != null && end != null && !type.equals("")) {
            if (title.equals("")) {
                title = "Not needed";
            }
            if (desc.equals("")) {
                desc = "Not needed";
            }
            if (loc.equals("")) {
                loc = "Not needed";
            }
            if (contact.equals("")) {
                contact = "Not needed";
            }
            if (url.equals("")) {
                url = "Not needed";
            }
            Appointment appt = new Appointment(custId, userId, title, desc, loc, contact,
                type, url, getUTC(date, start), getUTC(date, end));
            return appt;
        } else {
            return null;
        }   
    }
    
    public ZonedDateTime getUTC(LocalDate date, String time) {
        String period = time.split(" ", 2)[1];
        int hour = Integer.parseInt(time.split(" ", 2)[0].split(":", 2)[0]);
        String minute = time.split(" ", 2)[0].split(":", 2)[1];
        if (period.equals("PM")){
            if (hour == 12) {
                return TimeConverter.getUTCTime(date + " " + hour + ":" + minute);
            } else {
                hour += 12;
                return TimeConverter.getUTCTime(date + " " + hour + ":" + minute);
            }
        } else {
            return TimeConverter.getUTCTime(date + " " + hour + ":" + minute);
        }
    }
    
    @FXML
    public void deleteAppt() {
        if (apptTableView.getSelectionModel().getSelectedItem() != null){
            Appointment appt = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
            try {
                DBConnection.connect();
                DBConnection.delete("appointment", "appointmentId = " + appt.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("failed to delete appointment");
            } finally {
                DBConnection.closeConn();
            }
            customerSelect();
        }
    }
    
    public void getUserName(String uName, int uId) {
        userName = uName;
        userId = uId;
        userNameLabel.setText("User: " + userName);
    }
    
    public void populateTimes() {
        ArrayList<String> times = new ArrayList<>();
        for (int x = 7; x < 13; x++) {
            String minutes;
            String hours;
            String period;
            hours = Integer.toString(x) + ":";
            for (int y = 0; y < 59; y += 15) {
                if (y == 0){
                    minutes = Integer.toString(y) + "0";
                } else {
                    minutes = Integer.toString(y);
                }
                if (x == 12) {
                    period = " PM";
                } else {
                    period = " AM";
                }
                times.add(hours + minutes + period);
            }
        }
        for (int x = 1; x < 5; x++) {
            for (int y = 0; y < 59; y += 15) {
                if (y == 0) {
                    times.add(Integer.toString(x) + ":" + Integer.toString(y) + "0 PM");
                } else {
                    times.add(Integer.toString(x) + ":" + Integer.toString(y) + " PM");
                }
                
            }
        }
        for (String time : times) {
            startTimeChoiceBox.getItems().add(time);
            endTimeChoiceBox.getItems().add(time);
        }
    }
    
    public void populateCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "customer");
            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                customers.add(new Customer(customerId, customerName));
            }
            customerComboBox.setConverter(new CustomerConverter());
            customerComboBox.setItems(observableList(customers));
        } catch (SQLException e) {
            System.out.println("failed to populate customer box");
        } finally {
            DBConnection.closeConn();
        }
    }
    
    
    public void populateApptTable(int CustomerId) {
        apptTableView.getItems().clear();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "appointment", "customerId=" + CustomerId);
            while (rs.next()) {
                obsApptList.add(DBConnection.getAppointment(rs));
            }
            dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
            typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
            apptTableView.setItems(obsApptList);
        } catch (SQLException e) {
            System.out.println("failed to populate table");
        } finally {
            DBConnection.closeConn();
        } 
    }
}
