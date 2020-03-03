/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package Models;

import java.util.Calendar;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class Customer {
    private int CustomerID;
    private String CustomerName;
    private int AddressID;
    private int Active;
    
    public Customer (int customerID, String customerName, int addressID, int active){
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddressID(addressID);
        setActive(active);
    }
    
    public Customer(int customerID, String customerName) {
        setCustomerID(customerID);
        setCustomerName(customerName);
    }
    
    public int getCustomerID () {
        return CustomerID;
    }
    
    public void setCustomerID (int custid) {
        CustomerID = custid;
    }
    
    public String getCustomerName() {
        return CustomerName;
    }
    
    public void setCustomerName(String name) {
        CustomerName = name;
    }
    
    public int getAddressID() {
        return AddressID;
    }
    
    public void setAddressID(int addID) {
        AddressID = addID;
    }
    
    public int getActive() {
        return Active;
    }
    
    public void setActive(int active) {
        Active = active;
    }
}
