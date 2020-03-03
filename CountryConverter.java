/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import Models.Country;
import javafx.util.StringConverter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class CountryConverter extends StringConverter<Country>{
    @Override
    public Country fromString(String countryAsString) {
        String[] parts = countryAsString.split(" ");
        return new Country(Integer.parseInt(parts[0]), parts[1]);
    }
    
    @Override
    public String toString(Country country) {
        return country.getCountryID() + " " + country.getCountryName();
    }
    
}
