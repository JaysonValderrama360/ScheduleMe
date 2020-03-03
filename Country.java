/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package Models;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class Country {
    private int CountryID;
    private String CountryName;
    
    public Country(int countryID, String countryName) {
        this.setCountryID(countryID);
        this.setCountryName(countryName);
    }
    
    public void setCountryID(int countryID) {
        CountryID = countryID;
    }
    
    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
    
    public int getCountryID() {
        return CountryID;
    }
    
    public String getCountryName() {
        return CountryName;
    }
}
