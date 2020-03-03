/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import Models.Appointment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class DBConnection {
    private static DBConnection dbconn;
    
    final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final static String DB_URL = "jdbc:mysql://52.206.157.109/U05lYc";
    final static String DBUSER = "U05lYc";
    final static String DBPASS = "53688540815";
    private static Connection conn;
    
    private DBConnection(){}
    
    public static DBConnection getDBConn() {
        return dbconn;
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public static void connect() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
            System.out.println("Connection successful");
        } catch (ClassNotFoundException e) {
            System.out.println("class not found error");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL connection failed");
            e.printStackTrace();
        }
    }
    
    public static void closeConn(){
        try{
            conn.close(); 
            System.out.println("Connection Closed");
        } catch (SQLException e) {
            System.out.println("failed to close SQL connection");
            e.printStackTrace();
        } 
    }
    
    public static ResultSet query(String selectValue, String fromValue, String whereValue) throws SQLException{
        Statement stmt;
        stmt = conn.createStatement();
        String query = "SELECT " + selectValue + " FROM " + fromValue + 
                " WHERE " + whereValue + ";";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }
    
    public static ResultSet query(String selectValue, String fromValue) throws SQLException{
        Statement stmt;
        stmt = conn.createStatement();
        String query = "SELECT " + selectValue + " FROM " + fromValue + ";";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }
    
    public static void insert(String TableName, String Values) throws SQLException{
        Statement stmt;
        stmt = conn.createStatement();
        String insert = "INSERT INTO " + TableName + " VALUES " + Values + ";";
        stmt.executeUpdate(insert);
    }
    
    public static void delete(String TableName, String Value) throws SQLException {
        Statement stmt;
        stmt = conn.createStatement();
        String delete = "DELETE FROM " + TableName + " WHERE " + Value + ";";
        stmt.executeUpdate(delete);
    }
    
    public static String dateConvert(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    
    public static void update(String TableName, String Values, String Where) throws SQLException {
        Statement stmt;
        stmt = conn.createStatement();
        String update = "UPDATE " + TableName + " SET " + Values + " WHERE " + Where + ";";
        stmt.executeUpdate(update);
    }
    
    public static Appointment getAppointment(ResultSet rs) throws SQLException{
        int apptId = rs.getInt("appointmentId");
        int customerId = rs.getInt("customerId");
        int userId = rs.getInt("userId");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String location = rs.getString("location");
        String contact = rs.getString("contact");
        String type = rs.getString("type");
        String url = rs.getString("url");
        ZonedDateTime start = TimeConverter.getLocalTime(rs.getString("start"));
        ZonedDateTime end = TimeConverter.getLocalTime(rs.getString("end"));
        return new Appointment(apptId, customerId, userId, title, description, location, contact, type, url, start, end);
    }
}
