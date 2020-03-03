/*
 * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package Models;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class User {
    private int UserID;
    private String UserName;
    
    public User(int id, String name) {
        setUserID(id);
        setUserName(name);
    }
    
    public void setUserID(int uid) {
        UserID = uid;
    }
    
    public int getUserID() {
        return UserID;
    }
    
    public void setUserName(String uName) {
        UserName = uName;
    }
    
    public String getUserName() {
        return UserName;
    }
}
