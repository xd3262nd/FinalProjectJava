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


    public static List<Tasks> getAllData() {

        List<Tasks> allRecords = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(dbURI);
             Statement stat = conn.createStatement()){

            String selectAllQ = ("SELECT rowid, * FROM todos WHERE status = 'INCOMPLETE' ORDER BY category, priority");

            ResultSet rsall = stat.executeQuery(selectAllQ);

            while(rsall.next()){
                int id = rsall.getInt("rowid");
                String name = rsall.getString("taskName");
                String desc = rsall.getString("description");
                int prio = rsall.getInt("priority");

                String category = rsall.getString("category");
                Tasks.TasksCategory cat = Tasks.TasksCategory.valueOf(category);

                String status = rsall.getString("status");
                Tasks.TasksStatus statusTasks = Tasks.TasksStatus.valueOf(status);

                Date dateCreated = new Date(rsall.getLong("dateCreated"));

                Date dateCompleted = new Date();

                if(rsall.getLong("dateCompleted")==0){
                    dateCompleted=null;
                }else{
                    dateCompleted =new Date(rsall.getLong("dateCompleted"));
                }

    //int id, String name, String desc, Date dateCreated, int p, TasksCategory cate, Date dateCompleted, TasksStatus status
                Tasks task = new Tasks(id,name, desc, dateCreated, prio, cat, dateCompleted, statusTasks);

                allRecords.add(task);


            }
            return allRecords;



        }catch(SQLException s){
            System.out.println(s.getMessage() + s);
            return null;
        }


    }


    static Vector getColumnNames() {
        Vector<String> columnNames = new Vector<>();

        columnNames.add("Tasks");
        columnNames.add("Description");
        columnNames.add("Priority");
        columnNames.add("Category");
        return columnNames;
    }
}
