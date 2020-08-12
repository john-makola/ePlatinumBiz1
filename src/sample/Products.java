package sample;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Products {
    private IntegerProperty productid;
    private StringProperty name;
    private  StringProperty description;
    private  StringProperty category;
    private IntegerProperty price;
    private StringProperty packaging;
    private IntegerProperty quantity;
    private IntegerProperty pimage;

// Constructor

    public Products(int productid, String name,String description,String category,int price,String packaging,int quantity){

        this.productid= new SimpleIntegerProperty(productid);
        this.name = new SimpleStringProperty(name);
        this.description=new SimpleStringProperty(description);
        this.category=new SimpleStringProperty(category);
        this.price = new SimpleIntegerProperty(price);
        this.packaging = new SimpleStringProperty(packaging);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Constructor Market

    public Products(int ID, String name,int price){
        this.productid = new SimpleIntegerProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);

    }



    // Getter Methods & Property Methods

    public int getProductid(){return productid.get();}
    public String getName() {
        return name.get();
    }


    public String getDescription() {
        return description.get();
    }
    public String getCategory() {
        return category.get();
    }

    public int getPrice() {
        return price.get();
    }

    public String getPackaging() {
        return packaging.get();
    }

    public int getQuantity() {
        return quantity.get();
    }


    //Setter Methods

    public void setProductid(int value){productid.set(value);};
    public void setName(String value) {
        name.set(value);
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public void setCategory(String value) {
        category.set(value);
    }

    public void setPrice(int value) {
       price.set(value);
    }

    public void setPackaging(String value) {
        packaging.set(value);
    }
        public void setQuantity(int value) {
            quantity.set(value);
        }

    // Property Methods

    public IntegerProperty productidProperty(){return productid;};
    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public StringProperty packagingProperty() {
        return packaging;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }



}

