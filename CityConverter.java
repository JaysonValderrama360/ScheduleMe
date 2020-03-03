/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import Models.City;
import javafx.util.StringConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class CityConverter extends StringConverter<City> {
    @Override
    public City fromString(String cityAsString) {
        String[] parts = cityAsString.split(" ");
        return new City(Integer.parseInt(parts[0]), parts[1]);
    }
    
    @Override
    public String toString(City city) {
        return city.getCityID() + " " + city.getCityName();
    }
}
