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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddDataTableviewController implements Initializable  {


    @FXML private TextField pPrice;
    @FXML private Label pTotalstext;
    @FXML private ComboBox<?> pCategory;
    @FXML private TextField pQuantity;
    @FXML private Label updatelabel;
    @FXML private Button pUploadimage;
    @FXML private TableView<Products> tableMain;
    @FXML private TableColumn<Products,String> pNameT;
    @FXML private TableColumn<Products,String> pDescriptionT;
    @FXML private TableColumn<Products,String> pcategoryT;
    @FXML private TableColumn<Products,Integer> pPriceT;
    @FXML private TableColumn<Products,String> pPackagingT;
    @FXML private TableColumn<Products,Integer> pQuantityT;
    @FXML private TableColumn<Products,Integer>productNo;
    @FXML private TextField pPackaging;
    @FXML private ImageView logoImageView;
    @FXML private Button puploadData;
    @FXML private Label pPriceerror;
    @FXML private TextField pName;
    @FXML private TextArea pDescription;
    @FXML private ImageView productmageView;
    @FXML private Button pCleardata;
    @FXML private Label pQuantityerror;
    @FXML private Label pNameerror;
    @FXML private AnchorPane tablepane;
    @FXML private AnchorPane pProductImageAnchor;
    @FXML private TextArea textArea;
    @FXML private MenuBar MainMenu;
    @FXML private Menu latestMenu;
    @FXML private Menu fileMenu;
    @FXML private Menu helpMenu;
    @FXML private Menu marketMenu;
    @FXML private Menu editMenu;
    @FXML private Menu settingMenu;


    private FileChooser fileChooser;
    private File Filechoosen;
    private Image image;
    private FileInputStream fis;



    DecimalFormat numberFormat = new DecimalFormat("#,###.00");
    //Observabable List for Table Data
    private ObservableList<Products> tableData;
    //Observabable List for Combobox
    ObservableList category = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Statements to Load Images on Fly
        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);

        File CartFile = new File("images/Cart.png");
        Image CartImage = new Image(CartFile.toURI().toString());
        productmageView.setImage(CartImage);

        tableMain.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMain.prefWidthProperty().bind(tablepane.widthProperty());

        //ComboBox
        fillCombobox();
        pCategory.getItems().clear();
        pCategory.setPromptText("Choose Product/ Service Category");
        pCategory.setEditable(true);
        pCategory.setItems(category);
        fillTable();

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

        /*S et on Action Method to upload and Image
        pUploadimage.setOnAction(e->{
            //File Chooser
            fileChooser = new FileChooser();
            Stage stage = (Stage) pProductImageAnchor.getScene().getWindow();
            File File = fileChooser.showOpenDialog(stage);
            if(File !=null){
                textArea.setText(File.getAbsolutePath());
                image = new Image(File.toURI().toString(),135,149,true,true);
                productmageView.setImage(image);
                productmageView.setFitWidth(135);
                productmageView.setFitHeight(149);
                productmageView.setPreserveRatio(true);

            }
        });*/

        /*marketMenu = new Menu();

        marketMenu.setOnAction(e->{
            System.out.println("urgnfdsgfkdgskgf");
            try {
                Parent View1 = FXMLLoader.load(getClass().getResource("MarketPlace.fxml"));
                Scene scene3 = new Scene(View1);
                Stage Window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                Window.setScene(scene3);
                Window.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        } );
        MainMenu.getMenus().add(marketMenu);*/
    }



    @FXML
    public void uploadimagem() {
        pUploadimage.setOnAction(e -> {
            //File Chooser
            fileChooser = new FileChooser();
            Stage stage = (Stage) pProductImageAnchor.getScene().getWindow();
            Filechoosen = fileChooser.showOpenDialog(stage);
            if (Filechoosen != null) {
                textArea.setText(Filechoosen.getAbsolutePath());
                image = new Image(Filechoosen.toURI().toString(), 135, 149, true, true);
                productmageView.setImage(image);
                productmageView.setFitWidth(135);
                productmageView.setFitHeight(149);
                productmageView.setPreserveRatio(true);
            }
        });
    }
        @FXML
        private void uploadData(ActionEvent event) throws IOException {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        tableData=FXCollections.observableArrayList();

        try {
            String query ="select * from products ";
            ResultSet rs = connectDB.createStatement().executeQuery( query);

            while (rs.next()) {
                tableData.add(new Products( rs.getInt(1),
                                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5),
                rs.getString(7),
                rs.getInt(6)));

            }
        } catch (SQLException e) {
            System.err.println("Error"+e);
        }
       productNo.setCellValueFactory(new PropertyValueFactory<>("productid"));
        pNameT.setCellValueFactory(new PropertyValueFactory<>("name"));
        pDescriptionT.setCellValueFactory(new PropertyValueFactory<>("description"));
        pcategoryT.setCellValueFactory(new PropertyValueFactory<>("category"));
        pPriceT.setCellValueFactory(new PropertyValueFactory<>("price"));
        pPackagingT.setCellValueFactory(new PropertyValueFactory<>("packaging"));
        pQuantityT.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableMain.setItems(null);
        tableMain.setItems(tableData);
    }

    //FillTable
    public void fillTable(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        tableData=FXCollections.observableArrayList();

        try {

            ResultSet rs = connectDB.createStatement().executeQuery( "select * from products ");

            while (rs.next()) {
                tableData.add(new Products(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(7),
                        rs.getInt(6)));

            }
        } catch (SQLException e) {
            System.err.println("Error"+e);
        }
        productNo.setCellValueFactory(new PropertyValueFactory<>("productid"));
        pNameT.setCellValueFactory(new PropertyValueFactory<>("name"));
        pDescriptionT.setCellValueFactory(new PropertyValueFactory<>("description"));
        pcategoryT.setCellValueFactory(new PropertyValueFactory<>("category"));
        pPriceT.setCellValueFactory(new PropertyValueFactory<>("price"));
        pPackagingT.setCellValueFactory(new PropertyValueFactory<>("packaging"));
        pQuantityT.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableMain.setItems(null);
        tableMain.setItems(tableData);
    }


    //Fill Combox
    public int fillCombobox(){

        int connection=0;
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = "select categoryname from category";

            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            ResultSet result = Statement1.executeQuery();
            while (result.next()){
                category.addAll(result.getString("categoryname"));
            }
            Statement1.close();
            result.close();

        } catch (SQLException e) {

        }
        return connection;
    }


    // Verify Data and Post to Database
    @FXML
    private void setpAddproduct(ActionEvent event) throws IOException {

        if (validateNameifNull(pName)&& isproductnameRegistered(pName) && validatedPrice(pPrice) && validatedQuantity(pQuantity)) {
            postDta();
            fillTable();
            clearFields();
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
            String query = " insert into products (pName,pDescription,pCategory,pPrice,pPackaging,pQuantity,pImage)"
                    + " values (?,?,?,?,?,?,?)";
            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            Statement1.setString(1, pName.getText());
            Statement1.setString(2,pDescription.getText());
            String value = pCategory.getSelectionModel().getSelectedItem().toString();
            Statement1.setString(3,value);
            Statement1.setInt(4, Integer.parseInt(pPrice.getText()));
            Statement1.setString(5,pPackaging.getText());
            Statement1.setInt(6, Integer.parseInt(pQuantity.getText()));

            File file=new File(String.valueOf(Filechoosen));
            FileInputStream fis=new FileInputStream(file);
            Statement1.setBinaryStream(7,fis,(int)file.length());

            int x = Statement1.executeUpdate();
            if(x > 0){
                clearFields();
                updatelabel.setText("Record updated");

            } else {
                updatelabel.setText("Record Update Failed");
            }
            connectDB.close();
        } catch (SQLException | FileNotFoundException e) {
            updatelabel.setText("Try Again");
            e.printStackTrace();
        }
        return connection;
    }


    // Method Validate if Data Entered is Priceis a Number.
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


    // Method Validate if Data Entered is Quantity is a Number.
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


    // Method Validate if Data name is Unique.
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
    @FXML
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
        pCategory.setPromptText("Choose you Product/ Service");
    }

    // Open Add New Products
    @FXML private void addProducts(ActionEvent event) throws IOException {

        Parent View1 = FXMLLoader.load(getClass().getResource("ViewProducts.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }

    @FXML public void marketPlace(ActionEvent event) throws IOException {

       Parent View1 = FXMLLoader.load(getClass().getResource("MarketPlace.fxml"));
        Scene scene3 = new Scene(View1);
        Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene3);
        Window.show();

    }

}


