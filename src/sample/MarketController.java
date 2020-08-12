package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class MarketController implements Initializable {

    @FXML private TableView<Products> tableMain;
    @FXML private TableView<sales> tableMain1;
    @FXML private TableColumn<sales,Integer>tproductID1;
    @FXML private TableColumn<sales,String>pNameT1;
    @FXML private TableColumn<sales,Integer>pPriceT1;
    @FXML private TableColumn<sales,Integer>pQuantity1;
    @FXML private TableColumn<Products,Integer>pIDT;
    @FXML private TableColumn<Products,String> pNameT;
    @FXML private TableColumn<Products,String> pDescriptionT;
    @FXML private TableColumn<Products,String> pcategoryT;
    @FXML private TableColumn<Products,Integer> pPriceT;
    @FXML private TableColumn<Products,String> pPackagingT;
    @FXML private TableColumn<Products,Integer> pQuantityT;
    @FXML private TableColumn<Products,Integer>productNo;
    @FXML private AnchorPane tablepane;
    @FXML private ImageView cartImage;
    @FXML private TextField tproductId;
    @FXML private TextField tName;
    @FXML private TextField tPrice;
    @FXML private TextField tQuantity;
    @FXML private TextField searchitems;
    @FXML private Button addbutton;
    @FXML private Button checkout;
    @FXML private Button closebutton;
    @FXML private Button deletebutton;
    @FXML private Button searchButton;
    @FXML private ImageView search;
    @FXML private ImageView emptycart;
    @FXML private ImageView filledcart;
    @FXML private ImageView logoImageView;
    @FXML private ImageView home;
    @FXML private ImageView close;
    @FXML private ImageView delete;
    @FXML private ImageView add;
    @FXML private Label tTotal;
    @FXML private Label tTotal1;
    @FXML private Label time;






    DecimalFormat numberFormat = new DecimalFormat("#,###.00");
    //Observabable List for Table Data

    //Observabable List for Combobox
    ObservableList category = FXCollections.observableArrayList();
    private ObservableList<Products> tableData = FXCollections.observableArrayList();

    private ObservableList<sales> tableData2 = FXCollections.observableArrayList();
    @FXML private ListView listCategory = new ListView(category);



    //@FXML private TextArea textareaDisplay= new TextArea(category);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    //Display All Icon images on Market Place Dash Board
        File LogoFile = new File("images/Logo3.png");
        Image LogoImage = new Image(LogoFile.toURI().toString());
        logoImageView.setImage(LogoImage);

        File CartFile = new File("images/Cart.png");
        Image CartImage = new Image(CartFile.toURI().toString());
        cartImage.setImage(CartImage);

        File SearchFile = new File("images/Search.png");
        Image SearchImage = new Image(SearchFile.toURI().toString());
        search.setImage(SearchImage);

        File emptycartFile = new File("images/emptycart.png");
        Image emptycartImage = new Image(emptycartFile.toURI().toString());
        emptycart.setImage(emptycartImage);

        File filledcartFile = new File("images/filledcart.png");
        Image filledcartImage = new Image(filledcartFile.toURI().toString());
        filledcart.setImage(filledcartImage);

        File homeFile = new File("images/home.png");
        Image homeImage = new Image(homeFile.toURI().toString());
        home.setImage(homeImage);

        File closeFile = new File("images/close.png");
        Image closeImage = new Image(closeFile.toURI().toString());
        close.setImage(closeImage);

        File deleteFile = new File("images/delete.png");
        Image deleteImage = new Image(deleteFile.toURI().toString());
        delete.setImage(deleteImage);

        File addFile = new File("images/addicon.png");
        Image addImage = new Image(addFile.toURI().toString());
        add.setImage(addImage);


//Fill Top Table with Data
        fillTable();
        tableMain.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //tableMain.prefWidthProperty().bind(tablepane.widthProperty());

//Display Selected Product Image
      tableMain.setOnMouseClicked(e->{
          DatabaseConnection connectNow = new DatabaseConnection();
          Connection connectDB = connectNow.getConnection();

            try {

                Products products = tableMain.getSelectionModel().getSelectedItem();
                String query = "select * from products where idproducts = ? ";
                PreparedStatement Statement1 = connectDB.prepareStatement(query);
                Statement1.setInt(1,products.getProductid());

                ResultSet rs = Statement1.executeQuery();


                while (rs.next()) {
                    tproductId.setText(rs.getString("idproducts"));
                    tName.setText(rs.getString("pName"));
                    tPrice.setText(rs.getString("pPrice"));

                    InputStream is = rs.getBinaryStream("pImage");
                    OutputStream os = new FileOutputStream(new File("Photo.jpg"));
                    byte [] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content))!=-1){

                        os.write(content,0,size);
                    }
                    os.close();
                    is.close();
                    Image image = new Image("file:Photo.jpg",211,203,true,true);
                    cartImage.setImage(image);
                }
            } catch (SQLException | FileNotFoundException ef) {
                System.err.println("Error"+e);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

      });


//Delete Selected Items from My Basket
        deletebutton.setOnAction(e -> {
            sales selectedItem = tableMain1.getSelectionModel().getSelectedItem();
            tableMain1.getItems().remove(selectedItem);
        });


// Statements to compute Totals on Fly
        for (int i = 0; i < 10; i++) {

            final int index = i;

            tPrice.textProperty().addListener((obs, oldVal, newVal) -> {
                //System.out.println("Text of Textfield on index " + index + " changed from " + oldVal
                //      + " to " + newVal);
                double Totals = Integer.parseInt(newVal) * Integer.parseInt(String.valueOf(tQuantity.getText()));
                tTotal.setText(String.valueOf(numberFormat.format(Totals)));
                tQuantity.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    //  System.out.println("Text of Textfield on index " + index + " changed from " + oldValue
                    //          + " to " + newValue);
                    double Totals2 = Integer.parseInt(newVal) * Integer.parseInt(newValue);
                    tTotal.setText(String.valueOf(numberFormat.format(Totals2)));
                });
            });

        }



        /*FilteredList<Products> filtereddata = new FilteredList<>(tableData , e->true);
        searchitems.setOnKeyReleased(e->{
            searchitems.textProperty().addListener((observableValue, oldValue,newValue) -> {
                filtereddata.setPredicate((product ->{
                   if(newValue ==null || newValue.isEmpty()){
                       filteredData.setPredicate(s -> s.contains(filter));
                       return true;
                   }
                   String lowercaseFilter = newValue.toLowerCase();
                   if((String.valueOf(product.getProductid(newValue)).indexOf(lowercaseFilter)!=-1)){
                       return true;
                   }else if (String.valueOf((product.getName(newValue).toLowerCase().indexOf(lowercaseFilter)!=-1){
                       return true;
                    }
                    return false;
            }));
        });*/


//Display Time and Date
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
            time.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


    }

    //FillTable Top Table
    public void fillTable(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        tableData=FXCollections.observableArrayList();

        try {

            ResultSet rs = connectDB.createStatement().executeQuery( "select * from products ");

            while (rs.next()) {
                tableData.add(new Products(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(5)));
            }
        } catch (SQLException e) {
            System.err.println("Error"+e);
        }
        pIDT.setCellValueFactory(new PropertyValueFactory<>("productid"));
        pNameT.setCellValueFactory(new PropertyValueFactory<>("name"));
        pPriceT.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableMain.setItems(null);
        tableMain.setItems(tableData);
    }



    //add Data to my Basket Table View

        public void addRecord(){
        sales sale = new sales();
        sale.setProductid(Integer.parseInt(tproductId.getText()));
        sale.setName(tName.getText());
        sale.setPrice(Integer.parseInt(tPrice.getText()));
        sale.setQuantity(Integer.parseInt(tQuantity.getText()));

            tableMain1.getItems().add(sale);
            tproductID1.setCellValueFactory(new PropertyValueFactory<>("productid"));
            pNameT1.setCellValueFactory(new PropertyValueFactory<>("name"));
            pPriceT1.setCellValueFactory(new PropertyValueFactory<>("price"));
            pQuantity1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            int total = 0 ;
            for (sales price : tableMain1.getItems()) {
                price.setTotal(price.getPrice()*price.getQuantity());
                total = (int) (total + price.getTotal());
                tTotal1.setText(numberFormat.format(total));
            }

        }


    //add Data to my Basket Table View
    @FXML
    private void settoBasket(ActionEvent event) throws IOException {
        addRecord();
        postDta();
        clearFields();
        }

    //Close Button
    //Cancel Button Method
    @FXML
    public void closewindow(ActionEvent event){
        Stage stage = (Stage) closebutton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void gohome(ActionEvent event) throws IOException {
        Parent View2 = FXMLLoader.load(getClass().getResource("DashScreen.fxml"));
        Scene scene2 = new Scene(View2);
        Stage Window = (Stage)((Node) event.getSource()).getScene().getWindow();

        Window.setScene(scene2);
        Window.show();

    }

    @FXML
    public void clearFields(){
        tproductId.setText(null);
        tName.setText(null);
        tQuantity.setText("1");
        tPrice.setText("0");
    }

    public int postDta() {
        int connection = 0;
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = " insert into sales (itemName,price,quantity)"
                    + " values (?,?,?)";
            PreparedStatement Statement1 = connectDB.prepareStatement(query);
            Statement1.setString(1, tName.getText());
            Statement1.setInt(2, Integer.parseInt(tPrice.getText()));
            Statement1.setInt(3, Integer.parseInt(tQuantity.getText()));

            int x = Statement1.executeUpdate();
            if(x > 0){
                clearFields();
                System.out.println("Record updated");

            } else {
                System.out.println("Record Update Failed");
            }
            connectDB.close();
        } catch (SQLException  e) {
            System.out.println("Try Again");
            e.printStackTrace();
        }
        return connection;
    }
    }



