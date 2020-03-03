/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package Models;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class City {
    private int CityID;
    private String CityName;
    
    public City(int cityID, String cityName) {
        this.setCityID(cityID);
        this.setCityName(cityName);
    }
    
    public void setCityID(int cityID) {
        CityID = cityID;
    }
    
    public int getCityID() {
        return CityID;
    }
    
    public void setCityName(String cityName) {
        CityName = cityName;
    }
    
    public String getCityName() {
        return CityName;
    }
}
