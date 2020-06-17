package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        String dabaseName = "eplatinumbiz";
        String dabaseUser = "root";
        String dabasePassword = "Wanyika@37";
        String url = "jdbc:mysql://localhost/" + dabaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,dabaseUser,dabasePassword);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
return databaseLink;
    }
}
