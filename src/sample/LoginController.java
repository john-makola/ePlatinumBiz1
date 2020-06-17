package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    //Declare Variables
    @FXML
    private  Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField enterPasswordField;
    @FXML
    private Button loginButton;



    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("images/LeftPanePic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File LockFile = new File("images/Padlock.png");
        Image LockImage = new Image(LockFile.toURI().toString());
        lockImageView.setImage(LockImage);
    }

    //Cancel Button Method
    public void setCancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Method for Login Label Text
    public void LoginButtonOnAction(ActionEvent event){

        if(usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false){
            validateLogin();
        }else{
            loginMessageLabel.setText("Please Enter Username and Password");
        }
    }


    //Validate user data
    public void validateLogin()  {
        DatabaseConnection  connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user_accounts WHERE username  = '" + usernameTextField.getText() + "' AND password = '" + enterPasswordField.getText() + "'";
        try{

            Statement Statement = connectDB.createStatement();
            ResultSet queryResult = Statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    loginMessageLabel.setText("Congrats");

                }else{
                    loginMessageLabel.setText("Invalid Logins");
                }
            }
        }catch (Exception e){
        e.printStackTrace();
        e.getCause();
        }
    }



}
