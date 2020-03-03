/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import util.DBConnection;
import util.TimeConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class Appointment {
    private int AppointmentID;
    private int CustomerID;
    private int UserID;
    private String Title;
    private String Description;
    private String Location;
    private String Contact;
    private String Type;
    private String URL;
    private ZonedDateTime Start;
    private ZonedDateTime End;
    
    
    public Appointment(int appointmentID, int custId, int userId, String title, String description,
            String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        setAppointmentID(appointmentID);
        setCustomerID(custId);
        setUserID(userId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setURL(url);
        setStart(start);
        setEnd(end);
    }
    
    public Appointment(int custId, int userId, String title, String description,
            String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        setCustomerID(custId);
        setUserID(userId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setURL(url);
        setStart(start);
        setEnd(end);
    }
    
    public int getAppointmentID() {
        return AppointmentID;
    }
    
    public void setAppointmentID(int appointmentID) {
        AppointmentID = appointmentID;
    }
    
    public int getCustomerID() {
        return CustomerID;
    }
    
    public void setCustomerID(int custId){
        CustomerID = custId;
    }
    
    public int getUserID() {
        return UserID;
    }
    
    public void setUserID(int userId) {
        UserID = userId;
    }
    
    public String getTitle() {
        return Title;
    }
    
    public void setTitle(String title) {
        Title = title;
    }
    
    public String getDescription() {
        return Description;
    }
    
    public void setDescription(String description) {
        Description = description;
    }
    
    public String getLocation() {
        return Location;
    }
    
    public void setLocation(String location) {
        Location = location;
    }
    
    public String getContact() {
        return Contact;
    }
    
    public void setContact(String contact) {
        Contact = contact;
    }
    
    public String getType() {
        return Type;
    }
    
    public void setType(String type) {
        Type = type;
    }
    
    public String getURL() {
        return URL;
    }
    
    public void setURL(String url) {
        URL = url;
    }
    
    public ZonedDateTime getStart() {
        return Start;
    }
    
    public void setStart(ZonedDateTime start) {
        Start = start;
    }
    
    public ZonedDateTime getEnd() {
        return End;
    }
    
    public void setEnd(ZonedDateTime end) {
        End = end;
    }
    @Override
    public String toString() {
        String startDT = TimeConverter.getDateTimeString(getStart());
        String endDT = TimeConverter.getDateTimeString(getEnd());
        String customerName = "failed to retrieve name";
        try {
            DBConnection.connect();
            ResultSet rs =DBConnection.query("*", "customer", "customerId = "+ getCustomerID());
            rs.next();
            customerName = rs.getString("customerName");
        } catch (SQLException e) {
            System.out.println("Failed to retried Customer Name");
        } finally {
            DBConnection.closeConn();
        }
        return customerName + " Start: " + startDT + " End: " + endDT + " Type: " + getType();
    }
}
