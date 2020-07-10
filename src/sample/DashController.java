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


public class DashController implements Initializable{
    //Declare Variables

    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView CartImageView;
    @FXML
    private Button updateProducts;


    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File LockFile = new File("images/Cart.png");
        Image LockImage = new Image(LockFile.toURI().toString());
        CartImageView.setImage(LockImage);

        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);
    }

    @FXML AddProductsController addProductsController;

// Open Add New Products
    @FXML
    private void addProducts(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("UpdateProducts.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }


}
