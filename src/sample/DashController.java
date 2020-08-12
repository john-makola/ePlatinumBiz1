package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DashController implements Initializable{
    //Declare Variables

    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView CartImageView;
    @FXML
    private Button updateProducts;

    @FXML
    private Button mostviewed;

    @FXML
    private AnchorPane mainpane;
    @FXML
    private Button market;

    @FXML
    private Button latestproducts;

    @FXML
    private GridPane gridPane;


    //Method to implement Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File LockFile = new File("images/Cart.png");
        Image LockImage = new Image(LockFile.toURI().toString());
        CartImageView.setImage(LockImage);

        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);



        market.prefWidthProperty().bind(gridPane.widthProperty());
        market.prefHeightProperty().bind(gridPane.heightProperty());
        updateProducts.prefWidthProperty().bind(gridPane.widthProperty());
       updateProducts.prefHeightProperty().bind(gridPane.heightProperty());
        latestproducts.prefWidthProperty().bind(gridPane.widthProperty());
        latestproducts.prefHeightProperty().bind(gridPane.heightProperty());
        mostviewed.prefWidthProperty().bind(gridPane.widthProperty());
        mostviewed.prefHeightProperty().bind(gridPane.heightProperty());
    }

    @FXML AddProductsController addProductsController;

// Open Add New Products
    @FXML private void addProducts(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("ViewProducts.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }

    @FXML private void marketPlace(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("MarketPlace.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }

}
