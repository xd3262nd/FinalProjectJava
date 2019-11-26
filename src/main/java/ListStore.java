import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ListStore {

    private String dbURI;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");


    ListStore(String databaseURI){
        this.dbURI = databaseURI;

        try(Connection conn = DriverManager.getConnection(databaseURI);
            Statement stat = conn.createStatement()){

            String createTable = "CREATE Table if not exists todos (taskName TEXT NOT NULL, description TEXT NOT NULL, priority INTEGER NOT NULL CHECK ( priority>=1 and priority<=5 ), category TEXT NOT Null Check(category = 'PERSONAL' or category ='WORK'), dateCreated INTEGER, dateCompleted INTEGER, status TEXT NOT NULL Check ( status = 'COMPLETED' or status='INCOMPLETE' ));";

            stat.executeUpdate(createTable);

        }catch (SQLException s){
            System.out.println(s);
        }
    }


    public  Vector<Vector> getAllincompleteData() {

//TODO need to work on this to get the incomplete Data
        try(Connection conn = DriverManager.getConnection(dbURI);
             Statement stat = conn.createStatement()){

            Vector<Vector> vectors = new Vector<>();

            return vectors;


        }catch (SQLException s){
            System.out.println(s);

            return null;
        }


    }

    public Vector<Vector> getAllcompletedData() {

        //TODO need to work on this to get all completed data

        try(Connection conn = DriverManager.getConnection(dbURI);
            Statement stat = conn.createStatement()){

            Vector<Vector> vectors = new Vector<>();

            return vectors;


        }catch (SQLException s){
            System.out.println(s);

            return null;
        }


    }




}
