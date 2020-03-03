/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package c195project;

import Models.City;
import Models.Country;
import Models.Customer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.CityConverter;
import util.CountryConverter;
import util.DBConnection;

/**
 * FXML Controller class
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class CustomersController implements Initializable {
    
    @FXML
    private TextField newNameTxtBox;
    @FXML
    private TextField newAddressTxtBox;
    @FXML
    private TextField newAddress2TxtBox;
    @FXML
    private TextField newPhoneTxtBox;
    @FXML
    private TextField newPostalCodeTxtBox;
    @FXML
    private ComboBox newCityComboBox;
    @FXML
    private ComboBox newCountryComboBox;
    @FXML
    private CheckBox newActiveChkBox;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private TextField updateIDTxtBox;
    @FXML
    private TextField updateNameTxtBox;
    @FXML
    private TextField updateAddressTxtBox;
    @FXML
    private TextField updateAddress2TxtBox;
    @FXML
    private TextField updatePhoneTxtBox;
    @FXML
    private TextField updatePostalCodeTxtBox;
    @FXML
    private ComboBox updateCityComboBox;
    @FXML
    private ComboBox updateCountryComboBox;
    @FXML
    private CheckBox updateActiveChkBox;
    @FXML
    private Button updateCustomerBtn;
    @FXML
    private Button deleteCustomerBtn;
    @FXML
    private TableView customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private Label userNameLabel;
    private String userName;
    
    private ArrayList<Customer> CustomerList = new ArrayList<>();
    private ObservableList<Customer> observableCustomer = FXCollections.observableList(CustomerList);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTable();
        populateCountry();
        addCustTableListener();
    }
    
    public void addCustTableListener() {
        customerTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue obs, Object oldSelection, Object newSelection) ->{
            if (newSelection != null) {
                Customer customer = (Customer) newSelection;
                updateIDTxtBox.setText(Integer.toString(customer.getCustomerID()));
                updateNameTxtBox.setText(customer.getCustomerName());
                if (customer.getActive() == 1) {
                    updateActiveChkBox.setSelected(true);
                } else {
                    updateActiveChkBox.setSelected(false);
                }
                try {
                    DBConnection.connect();
                    ResultSet rs = DBConnection.query("*", "address", "addressId=" + customer.getAddressID());
                    rs.next();
                    updateAddressTxtBox.setText(rs.getString("address"));
                    updateAddress2TxtBox.setText(rs.getString("address2"));
                    updatePostalCodeTxtBox.setText(rs.getString("postalCode"));
                    updatePhoneTxtBox.setText(rs.getString("phone"));                    
                } catch (SQLException e) {
                    System.out.println("failed to query DB");
                } finally {
                    DBConnection.closeConn();
                }
            }
        });
    }
    
    @FXML
    public void addCustomerBtnHandler() {
        String name = newNameTxtBox.getText();
        String Address = newAddressTxtBox.getText();
        String Address2 = newAddress2TxtBox.getText();
        String Phone = newPhoneTxtBox.getText();
        String PostalCode = newPostalCodeTxtBox.getText();
        City city = (City) newCityComboBox.getValue();
        Country country = (Country) newCountryComboBox.getValue();
        int active;
        if (newActiveChkBox.isSelected()) {
            active = 1;
        } else {
            active = 0;
        }
        
        String addressValues = "('" + Address + "','" + Address2 + "'," + city.getCityID() +
                ",'" + PostalCode + "','" + Phone + "', CURRENT_TIMESTAMP, 'test', CURRENT_TIMESTAMP, 'test')";
        if (name != null && Address != null && Phone != null && PostalCode != null) {
            try {
                DBConnection.connect();
                DBConnection.insert("address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) ",
                        addressValues);
                ResultSet rs = DBConnection.query("*","address", "address='" +Address + "'");
                rs.next();
                int AddressID = rs.getInt("addressId");
                String customerValues = "('" + name + "'," + AddressID + "," + active + ", CURRENT_TIMESTAMP, '" + userName + "', CURRENT_TIMESTAMP, '" + userName + "')";
                DBConnection.insert("customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)", customerValues);
            } catch (SQLException e) {
                System.out.println("Problem with DB insert");
                e.printStackTrace();
            } finally {
                DBConnection.closeConn();
            }
        }
    }
    
    @FXML
    public void updateCustomerBtnHandler() {
        String name = updateNameTxtBox.getText();
        String Address = updateAddressTxtBox.getText();
        String Address2 = updateAddress2TxtBox.getText();
        String Phone = updatePhoneTxtBox.getText();
        String PostalCode = updatePostalCodeTxtBox.getText();
        City city = (City) updateCityComboBox.getValue();
        Country country = (Country) updateCountryComboBox.getValue();
        int active;
        if (updateActiveChkBox.isSelected()) {
            active = 1;
        } else {
            active = 0;
        }
        if (!name.equals("") && !Address.equals("") && !Phone.equals("") && !PostalCode.equals("")
                && city != null && country != null) {
            if (customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
                try {
                    DBConnection.connect();
                    DBConnection.update("customer", "customerName='" + name + "', active=" + active + ", lastUpdate=CURRENT_TIMESTAMP,"
                            + "lastUpdateBy='" + userName + "' ", "customerId=" + customer.getCustomerID());
                    DBConnection.update("address", "address='" +Address+"',address2='"+Address2+"',postalCode='" +
                            PostalCode+"',phone='"+Phone+"',cityId="+city.getCityID()+",lastUpdate=CURRENT_TIMESTAMP,"
                                    + "lastUpdateBy='"+userName+"' ","addressId="+customer.getAddressID());
                } catch (SQLException e) {
                    System.out.println("update failed");
                } finally {
                    DBConnection.closeConn();
                }
                populateTable();
            }
        }
    }
    
    @FXML
    public void deleteCustomerBtnHandler() {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            try {
                DBConnection.connect();
                DBConnection.delete("customer", "customerId=" + customer.getCustomerID() + ";");
                DBConnection.delete("address", "addressId=" + customer.getAddressID());
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Problem deleting from database");
                alert.setContentText("Sorry, the customer could not be deleted");
                alert.showAndWait();
            } finally {
                DBConnection.closeConn();
            }
        }
    }
    
    @FXML
    public void newCountryComboBoxHandler() {
        if (newCountryComboBox.getSelectionModel().getSelectedItem() != null) {
            Country selectedCountry = (Country) 
                    newCountryComboBox.getSelectionModel().getSelectedItem();
            populateCity(selectedCountry.getCountryID(), newCityComboBox);
        }
    }
    
    @FXML
    public void updateCountryComboBoxHandler() {
        if (updateCountryComboBox.getSelectionModel().getSelectedItem() != null) {
            Country selectedCountry = (Country) 
                    updateCountryComboBox.getSelectionModel().getSelectedItem();
            populateCity(selectedCountry.getCountryID(), updateCityComboBox);
        }
    }
    
    // this function will be used to populate the country box on initialization
    public void populateCountry(){
        ArrayList<Country> countryList = new ArrayList();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "country");
            while (rs.next()) {
                int CountryID = rs.getInt("countryId");
                String CountryName = rs.getString("country");
                countryList.add(new Country(CountryID, CountryName));
            }
            newCountryComboBox.setConverter(new CountryConverter());
            newCountryComboBox.setItems(observableList(countryList));
            updateCountryComboBox.setConverter(new CountryConverter());
            updateCountryComboBox.setItems(observableList(countryList));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Problem retrieving city list");
            alert.setContentText("Sorry, the city list was unable to be retrieved");
            alert.showAndWait();
        } finally {
            DBConnection.closeConn();
        }
    }
    
    // this function will be used to populate the city box when country is selected
    public void populateCity(int countryID, ComboBox box){
        ArrayList<City> cityList = new ArrayList();
        try {
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "city", "countryId='" + countryID + "'");
            while (rs.next()) {
                int CityID = rs.getInt("cityId");
                String CityName = rs.getString("city");
                cityList.add(new City(CityID, CityName));
            }
            box.setConverter(new CityConverter());
            box.setItems(observableList(cityList));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR); 
            alert.setTitle("Error");
            alert.setHeaderText("Problem retrieving country list");
            alert.setContentText("Sorry, the country list was unable to be retrieved");
            alert.showAndWait();
        } finally {
            DBConnection.closeConn();
        }
    }
    
    public void populateTable() {
        try {
            customerTable.getItems().clear();
            DBConnection.connect();
            ResultSet rs = DBConnection.query("*", "customer");
            while (rs.next()){
                int customerID = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int addressId = rs.getInt("addressId");
                int active = rs.getInt("active");
                observableCustomer.add(new Customer(customerID, customerName, addressId, active));
            }
        } catch (SQLException e) {
            System.out.println("DB failed");
        } finally {
            DBConnection.closeConn();
        }
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerTable.setItems(observableCustomer);
    }
    
    public void getUserName(String uName) {
        userName = uName;
        userNameLabel.setText("User: " + userName);
    }
}
