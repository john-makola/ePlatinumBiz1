package sample;


import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController implements Initializable {

    //Declare Variables
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button signupButton;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField telNo;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label updatelabel;
    @FXML
    private Label telNoVerify;
    @FXML
    private Label passwordMatch;
    @FXML
    private Label passwordStrength;
    @FXML
    private Label usernameError;


    //Open Signup Window

    @FXML
    private void signUp(ActionEvent event) throws IOException {

        if(validatedNumber(telNo) && validatepass(password) &&  validatePassword(password) && isUsernameRegistered(username)){
            postDta();
        }
    }
@FXML
    private void Reset(ActionEvent event) throws IOException {
        clearFields();
    }



    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/LeftPanePic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);
    }

    //Cancel Button Method
    public void setCancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
// Method to Add Records to the mySQL Database
    public int postDta() {
        int connection = 0;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = " insert into signup (firstname,lastname,telno,username,password,confirmpassword)"
                    + " values (?, ?, ?, ?, ?,?)";
            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            Statement1.setString(1, firstname.getText());
            Statement1.setString(2, lastname.getText());
            Statement1.setInt(3, Integer.parseInt(telNo.getText()));
            Statement1.setString(4, username.getText());
            Statement1.setString(5, password.getText());
            Statement1.setString(6, confirmPassword.getText());

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
        }
        return connection;
    }

    // Method Validate if Data Entered is Numbers in the Telephone Field
    private boolean validatedNumber(TextField input) {
        Pattern p = Pattern.compile("[+-]?[0-9][0-9]*");
        Matcher m = p.matcher(input.getText());
        if (m.find() && m.group().equals(input.getText())) {
            telNoVerify.setText(null);
            return true;
        } else
            telNoVerify.setText("Enter Valid Digits");

        return false;
    }


    // Method Validate  if Password and Confirmation Password Matches
    private boolean validatePassword(TextField input){
        Pattern p = Pattern.compile(input.getText());
        Matcher m = p.matcher(confirmPassword.getText());
        if(m.matches()){
            passwordMatch.setText(null);
            return true;
        }else{
            passwordMatch.setText("Passwords don't Match");
            return false;
        }
    }

    // Method Clear all Text Fields after entry
    private void clearFields(){
        firstname.setText(null);
        lastname.setText(null);
        telNo.setText(null);
        username.setText(null);
        password.setText(null);
        confirmPassword.setText(null);
        telNoVerify.setText(null);
        passwordStrength.setText(null);
        passwordMatch.setText(null);
        usernameError.setText(null);

    }

    // Method Validate if if Password Matches Policy
    private boolean validatepass(TextField input){
        Pattern P = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])).{6,15}");
        Matcher m = P.matcher(input.getText());
        if(m.matches()){
            return true;
        }else {
            passwordStrength.setText("Weak Password");
            return false;
        }
    }


    //Method to Verify username entered does not exist on database

    public boolean isUsernameRegistered(TextField username) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT * FROM signup WHERE username='"+ username +"'";
        try {
            Statement stmt = connectDB.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {

            }else{
                usernameError.setText("Username Exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }return true;
    }

    }

