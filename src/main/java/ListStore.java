import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ListStore {

    private static String dbURI;
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


    public static Vector<Vector> getAllincompleteData() {

//TODO need to work on this to get the incomplete Data
        String sqlQuery = "SELECT * FROM todos WHERE status='INCOMPLETE'";
        try(Connection conn = DriverManager.getConnection(dbURI);
             Statement stat = conn.createStatement()){

            ResultSet rsIncomData = stat.executeQuery(sqlQuery);
            Vector<Vector> vectors = new Vector<>();

            /*
            Tasks
Desc
P
C
             */

            while(rsIncomData.next()){
                String name = rsIncomData.getString("taskName");
                String desc = rsIncomData.getString("description");
                int prio = rsIncomData.getInt("priority");
                String category = rsIncomData.getString("category");

                Vector v = new Vector();
                v.add(name);
                v.add(desc);
                v.add(prio);
                v.add(category);

                vectors.add(v);
            }


            return vectors;


        }catch (SQLException s){
            System.out.println(s);

            return null;
        }


    }

    public static Vector<Vector> getAllcompletedData() {
        String sqlQuery = "SELECT * FROM todos WHERE status='COMPLETED'";

        //TODO need to work on this to get all completed data

        try(Connection conn = DriverManager.getConnection(dbURI);
            Statement stat = conn.createStatement()){
            ResultSet rsIncomData = stat.executeQuery(sqlQuery);

            Vector<Vector> vectors = new Vector<>();

            while(rsIncomData.next()){
                String name = rsIncomData.getString("taskName");
                String desc = rsIncomData.getString("description");
                int prio = rsIncomData.getInt("priority");
                String category = rsIncomData.getString("category");

                Vector v = new Vector();
                v.add(name);
                v.add(desc);
                v.add(prio);
                v.add(category);

                vectors.add(v);
            }

            return vectors;

        }catch (SQLException s){
            System.out.println(s);

            return null;
        }


    }




}
