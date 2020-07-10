package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.cert.PolicyNode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class AddProductsController<pCategory> implements Initializable {
    @FXML
    private TextField pName;
    @FXML
    private TextArea pDescription;
    @FXML
    private ComboBox <String> pCategory;
    @FXML
    private TextField pPrice;
    @FXML
    private TextField pPackaging;
    @FXML
    private TextField pQuantity;
    @FXML
    private Button pCleardata;
    @FXML
    private Button pUploadfile;
    @FXML
    private Button pAddproduct;
    @FXML
    private Button pCancel;
    @FXML
    private Button pTotals;
    @FXML
    private Label pTotalstext;
    @FXML
    private ImageView pImage;
    @FXML
    private Label updatelabel;
    @FXML
    private Label pPriceerror;
    @FXML
    private Label pQuantityerror;
    @FXML
    private Label pNameerror;
    @FXML
    private ImageView logoImageView;
    @FXML
    private HBox hBox;

    DecimalFormat numberFormat = new DecimalFormat("#,###.00");
    ArrayList<TextField> myTextFields = new ArrayList<>();
    List<String> list;
    ObservableList category = FXCollections.observableArrayList();
    ResultSet result;


    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        //Statements to Load Images on Fly
        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);

        // Statements to compute Totals on Fly
        for (int i = 0; i < 10; i++) {

            final int index = i;

            pPrice.textProperty().addListener((obs, oldVal, newVal) -> {
                //System.out.println("Text of Textfield on index " + index + " changed from " + oldVal
                  //      + " to " + newVal);
                double Totals = Integer.parseInt(newVal) * Integer.parseInt(String.valueOf(pQuantity.getText()));
                pTotalstext.setText(String.valueOf(numberFormat.format(Totals)));
                pQuantity.textProperty().addListener((observableValue, oldValue, newValue) -> {
                  //  System.out.println("Text of Textfield on index " + index + " changed from " + oldValue
                  //          + " to " + newValue);
                double Totals2 = Integer.parseInt(newVal) * Integer.parseInt(newValue);
                pTotalstext.setText(String.valueOf(numberFormat.format(Totals2)));
                });
            });
        }

        //ComboBox
        //List<String> list = new ArrayList<String>();
        ///list.add("Fruits");
        //list.add("Grains");
        //list.add("Groceries");
        fillCombobox();
        pCategory.getItems().clear();
        pCategory.setPromptText("Choose Product/ Service Category");
        pCategory.setEditable(true);
        pCategory.setItems(category);
    }

    //Fill Combox
    public int fillCombobox(){

     int connection=0;
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = "select categoryname from category";

            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            result = Statement1.executeQuery();
            while (result.next()){
                category.addAll(result.getString("categoryname"));
             }
            Statement1.close();
            result.close();

        } catch (SQLException e) {
            updatelabel.setText("Try Again");
        }
        return connection;
    }




    // Verify Data and Post to Database
    @FXML
    private void setpAddproduct(ActionEvent event) throws IOException {

        if( isproductnameRegistered(pName) && validatedPrice(pPrice) && validatedQuantity(pQuantity)){
            postDta();
        }

        }


    // Method to Add Records to the mySQL Database
    public int postDta() {
        int connection = 0;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = " insert into products (pName,pDescription,pCategory,pPrice,pQuantity)"
                    + " values (?,?,?,?,?)";
            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            Statement1.setString(1, pName.getText());
            Statement1.setString(2,pDescription.getText());
            String value = pCategory.getSelectionModel().getSelectedItem().toString();
            Statement1.setString(3,value);
            Statement1.setInt(4, Integer.parseInt(pPrice.getText()));
            Statement1.setInt(5, Integer.parseInt(pQuantity.getText()));
            //Statement1.setString(4, username.getText());
            //Statement1.setString(5, password.getText());
            //Statement1.setString(6, confirmPassword.getText());

            int x = Statement1.executeUpdate();
            if(x > 0){
                clearFields();
                updatelabel.setText("Record updated");

            } else {
                updatelabel.setText("Record Update Failed");
            }
            connectDB.close();
        } catch (SQLException e) {
            updatelabel.setText("Try Again");
            e.printStackTrace();
        }
        return connection;
    }


    // Method Validate if Data Entered is Price & Telephone Fields are Nos.
    private boolean validatedPrice(TextField input) {
        Pattern p = Pattern.compile("[+-]?[0-9][0-9]*");
        Matcher m = p.matcher(input.getText());
        Matcher k = p.matcher(input.getText());
        if (m.find() && m.group().equals(input.getText())) {
            //p.setText(null);
            return true;
        } else
            pPriceerror.setText("Enter Valid No");
        return false;
    }

    private boolean validatedQuantity(TextField input) {
        Pattern p = Pattern.compile("[+-]?[0-9][0-9]*");
        Matcher m = p.matcher(input.getText());
        Matcher k = p.matcher(input.getText());
        if (m.find() && m.group().equals(input.getText())) {
            //p.setText(null);
            return true;
        } else
        pQuantityerror.setText("Enter Valid No");
        return false;
    }
//Method to Verify Product Name entered does not exist on database

    public boolean isproductnameRegistered(TextField pName) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT * FROM products WHERE pName='"+ pName +"'";
        try {
            Statement stmt = connectDB.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {

            }else {
                pNameerror.setText("Product Exists");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }return true;
    }


// Clear All Fields After Data Entry
    public void clearFields(){
        pName.setText(null);
        pDescription.setText(null);
        pPackaging.setText(null);
        pQuantity.setText("1");
        pPrice.setText("0");
        pQuantityerror.setText(null);
        pPriceerror.setText(null);
        pNameerror.setText(null);
        pTotalstext.setText("0"+"."+"00");
        pCategory.setValue(null);
       // pCategory.setPromptText("Choose you Product/ Service");

    }




}
