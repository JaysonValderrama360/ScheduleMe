/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import Models.User;
import javafx.util.StringConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class UserConverter extends StringConverter<User>{
    @Override
    public User fromString(String customerAsString) {
        String[] parts = customerAsString.split(" ");
        return new User(Integer.parseInt(parts[0]), parts[1]);
    }
    
    @Override
    public String toString(User user) {
        return user.getUserID() + " " + user.getUserName();
    }
}
