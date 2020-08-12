package sample;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class selectedProducts {
    private IntegerProperty productid1;
    private StringProperty name;
    private IntegerProperty price;
    private IntegerProperty quantity;



    // Constructor Market

    public selectedProducts( int idproducts, String name, int price,int quantity){
        this.productid1 = new SimpleIntegerProperty(idproducts);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }


    // Getter Methods & Property Methods

    public int getProductid(){return productid1.get();}
    public String getName() {
        return name.get();
    }
    public int getPrice() {
        return price.get();
    }
    public int getQuantity() {
        return quantity.get();
    }


    //Setter Methods

    public void setProductid(int value){productid1.set(value);};
    public void setName(String value) {
        name.set(value);
    }
    public void setPrice(int value) {
       price.set(value);
    }
        public void setQuantity(int value) {
            quantity.set(value);
        }

    // Property Methods

    public IntegerProperty productidProperty(){return productid1;};
    public StringProperty nameProperty() {
        return name;
    }
    public IntegerProperty priceProperty() {
        return price;
    }
    public IntegerProperty quantityProperty() {
        return quantity;
    }

}

