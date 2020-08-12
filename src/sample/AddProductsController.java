package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddProductsController implements Initializable {
    @FXML private TextField pName;
    @FXML private TextArea pDescription;
    @FXML private ComboBox <String> pCategory;
    @FXML private TextField pPrice;
    @FXML private TextField pPackaging;
    @FXML private TextField pQuantity;
    @FXML private Button pCleardata;
    @FXML private Button pUploadfile;
    @FXML private Button pAddproduct;
    @FXML private Button pCancel;
    @FXML private Button pTotals;
    @FXML private Label pTotalstext;
    @FXML private ImageView pImage;
    @FXML private Label updatelabel;
    @FXML private Label pPriceerror;
    @FXML private Label pQuantityerror;
    @FXML private Label pNameerror;
    @FXML private ImageView logoImageView;
    @FXML private HBox hBox;
    @FXML private Button pUploadimage;
    @FXML private ImageView productmageView;
    @FXML private AnchorPane pProductImageAnchor;
    @FXML private TextArea textArea;
    private FileChooser fileChooser;

    private FileInputStream fis;
    private File File;

    private Image image;


    DecimalFormat numberFormat = new DecimalFormat("#,###.00");
    ArrayList<TextField> myTextFields = new ArrayList<>();
    List<String> list;
    ObservableList category = FXCollections.observableArrayList();
    ResultSet result;
    @FXML Main main;

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

            pUploadimage.setOnAction(e->{
                System.out.println("Test");
                //File Chooser
                fileChooser = new FileChooser();
                Stage stage = (Stage) pProductImageAnchor.getScene().getWindow();
                File File = fileChooser.showOpenDialog(stage);
                if(File !=null){
                    textArea.setText(File.getAbsolutePath());
                    image = new Image(File.toURI().toString(),135,149,true,true);
                    System.out.println("Test");
                    productmageView = new ImageView(image);
                    productmageView.setFitWidth(135);
                    productmageView.setFitHeight(149);
                    productmageView.setPreserveRatio(true);
                }
            });

        }

        //ComboBox
        fillCombobox();
        pCategory.getItems().clear();
        pCategory.setPromptText("Choose Product/ Service Category");
        pCategory.setEditable(true);
        pCategory.setItems(category);
    }

    @FXML
    private void Cancel(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("DashScreen.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }

    public void viewProductTable(ActionEvent event) throws IOException {
        Parent View1 = FXMLLoader.load(getClass().getResource("ViewProducts.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();
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

            if (isproductnameRegistered(pName) && validatedPrice(pPrice) && validatedQuantity(pQuantity)) {
                postDta();
                Parent View1 = FXMLLoader.load(getClass().getResource("ViewProducts.fxml"));
                Scene scene3 = new Scene(View1);
                Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Window.setScene(scene3);
                Window.show();

            } else {
                updatelabel.setText("Fill Data Correctly and Submit");
            }

    }

    // Method to Add Records to the mySQL Database
    public int postDta() {
        int connection = 0;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = " insert into products (pName,pDescription,pCategory,pPrice,pPackaging,pQuantity)"
                    + " values (?,?,?,?,?,?)";
            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            Statement1.setString(1, pName.getText());
            Statement1.setString(2,pDescription.getText());
            String value = pCategory.getSelectionModel().getSelectedItem().toString();
            Statement1.setString(3,value);
            Statement1.setInt(4, Integer.parseInt(pPrice.getText()));
            Statement1.setString(5,pPackaging.getText());
            Statement1.setInt(6, Integer.parseInt(pQuantity.getText()));


            //fis = new FileInputStream(File);
           //Statement1.setBinaryStream(6,(InputStream)fis,(int)File.length());
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
        } catch (SQLException  e) {
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

    private boolean validateNameifNull(TextField input) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input.getText());
        Matcher k = p.matcher(input.getText());
        if (m.find() && m.group().equals(input.getText())) {
            //p.setText(null);
            return true;
        } else
            pNameerror.setText("Enter Valid Product");
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
