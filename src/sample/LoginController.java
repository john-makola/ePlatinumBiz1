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
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private ImageView logoImageView;
    @FXML
    private Button signupButton;

    //Open Signup Window

@FXML SignupController signupController;


    @FXML
    private void signUp(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("Signup.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

}
    @FXML DashController dashController;
    @FXML
    private void dash(ActionEvent event) throws IOException {

        if (validateLogin()) {
            System.out.println("Sawa");
            Parent View2 = FXMLLoader.load(getClass().getResource("DashScreen.fxml"));
            Scene scene2 = new Scene(View2);
            Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Window.setScene(scene2);
            Window.show();
        }
    }



//Open DashBoard Window
    @FXML
    private void shopOnline(ActionEvent event) throws IOException {
       validateLogin();
           Parent View2 = FXMLLoader.load(getClass().getResource("DashScreen.fxml"));
           Scene scene2 = new Scene(View2);
           Stage Window = (Stage)((Node) event.getSource()).getScene().getWindow();

           Window.setScene(scene2);
           Window.show();

    }



    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("images/LeftPanePic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File LockFile = new File("images/Padlock.png");
        Image LockImage = new Image(LockFile.toURI().toString());
        lockImageView.setImage(LockImage);

       File LogoFile = new File("images/Logo3.png");
       Image LogoImage = new Image(LogoFile.toURI().toString());
       logoImageView.setImage(LogoImage);
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
    public boolean  validateLogin()  {
        DatabaseConnection  connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        //boolean answer = false;

        String verifyLogin = "SELECT count(1) FROM signup WHERE username  = '" + usernameTextField.getText() + "' AND password = '" + enterPasswordField.getText() + "'";
        try{

            Statement Statement = connectDB.createStatement();
            ResultSet queryResult = Statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    loginMessageLabel.setText("Congrats");
                    //answer =true;
                }else{
                    loginMessageLabel.setText("Invalid Logins");
                }
            }
        }catch (Exception e){
        e.printStackTrace();
        e.getCause();
        }
        return true;
    }



}
