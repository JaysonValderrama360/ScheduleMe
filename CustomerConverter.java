/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import Models.Customer;
import javafx.util.StringConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class CustomerConverter extends StringConverter<Customer>{
    @Override
    public Customer fromString(String customerAsString) {
        String[] parts = customerAsString.split(" ");
        return new Customer(Integer.parseInt(parts[0]), parts[1]);
    }
    
    @Override
    public String toString(Customer customer) {
        return customer.getCustomerID() + " " + customer.getCustomerName();
    }
}
