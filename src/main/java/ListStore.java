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

    public static void add(Tasks newTasks) throws SQLException {

        Connection conn = DriverManager.getConnection(dbURI);
        PreparedStatement pst = conn.prepareStatement("insert into todos values(?,?,?,?,?,?,?)");

        //n, d, p, c, dc, dc, s
        String name = newTasks.getName();
        String desc = newTasks.getDescription();
        int priority = newTasks.getPriority();
        String category = String.valueOf(newTasks.getCategory());

        Long dateCreatedInt = newTasks.getDateCreated().getTime();

        String status = String.valueOf(newTasks.getStatus());

        pst.setString(1, name);
        pst.setString(2, desc);
        pst.setInt(3, priority);
        pst.setString(4, category);
        pst.setLong(5, dateCreatedInt);
        pst.setString(7,status);
        if(newTasks.getDateCompleted()==null){
            pst.setLong(6,0);
        }else{
            Long dateCompletedInt = newTasks.getDateCompleted().getTime();
            pst.setLong(6,dateCompletedInt);
        }
        pst.execute();

        Statement s = conn.createStatement();
        ResultSet generateID = s.getGeneratedKeys();

        while(generateID.next()){
            int rowID = generateID.getInt(1);
            newTasks.setTasksID(rowID);
        }
    }

    public static Tasks getTasksInfo(int id) {

        try(Connection c = DriverManager.getConnection(dbURI);
             PreparedStatement ps = c.prepareStatement("SELECT * FROM todos WHERE rowid = ?")){

            ResultSet rs;

            ps.setInt(1, id);

            rs=ps.executeQuery();

            if(rs.next()==false){
                return null;
            }else{
                //dc, p, dc, s
                String name = rs.getString("taskName");
                String desc = rs.getString("description");

                Date dateCreated = new Date(rs.getLong("dateCreated"));

                int priority = rs.getInt("priority");

                String categoryDB = rs.getString("category");
                Tasks.TasksCategory categoryTasks = Tasks.TasksCategory.valueOf(categoryDB);

                Date dateCompleted = new Date(rs.getLong("dateCompleted"));

                String statusDB = rs.getString("status");
                Tasks.TasksStatus statusT = Tasks.TasksStatus.valueOf(statusDB);


                Tasks t = new Tasks(id, name, desc,dateCreated, priority, categoryTasks, dateCompleted, statusT);
                return t;
            }



        }catch(SQLException s){
            System.out.println(s.getMessage()+s);
            return null;
        }
    }


    public static void updateTasks(Tasks task) {

        try(Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement pst = conn.prepareStatement("UPDATE todos SET taskName =?, description =?, dateCreated=?, priority=?, category=?, dateCompleted=?, status=? WHERE rowid =?")){

            int id = task.getTasksID();
            String name = task.getName();
            String desc = task.getDescription();

            int priorityDB =task.getPriority();

            String categoryDB = String.valueOf(task.getCategory());

            String statusDB = String.valueOf(task.getStatus());

            Long dateCreatedDB = task.getDateCreated().getTime();

            pst.setString(1, name);
            pst.setString(2, desc);
            pst.setLong(3, dateCreatedDB);
            pst.setInt(4, priorityDB);
            pst.setString(5, categoryDB);
            if(task.getDateCompleted()==null){
                pst.setLong(6,0);
            }else{
                Long dateCompletedDB = task.getDateCompleted().getTime();
                pst.setLong(6,dateCompletedDB);
            }


            pst.setString(7, statusDB);
            pst.setInt(8, id);

            pst.executeUpdate();

        }catch(SQLException se){
            System.out.println(se.getMessage()+se);
        }
    }

    public static List<Tasks> searchByPriority(int p) {

        try(Connection c = DriverManager.getConnection(dbURI);
            PreparedStatement pst = c.prepareStatement("SELECT rowid,* FROM todos WHERE priority= ? AND status = ? ORDER BY category")){

            pst.setInt(1, p);
            pst.setString(2, "INCOMPLETE");

            List<Tasks> searchList = new ArrayList<>();

            ResultSet rsP = pst.executeQuery();

            while(rsP.next()){
                int id = rsP.getInt("rowid");
                String name = rsP.getString("taskName");
                String desc = rsP.getString("description");
                Date dateCreated = new Date(rsP.getLong("dateCreated"));
                int priority = rsP.getInt("priority");
                String categoryDB = rsP.getString("category");
                Tasks.TasksCategory categoryT = Tasks.TasksCategory.valueOf(categoryDB);
                Date dateCompleted = new Date(rsP.getLong("dateCompleted"));
                String statusDB = rsP.getString("status");
                Tasks.TasksStatus statusT = Tasks.TasksStatus.valueOf(statusDB);

                Tasks t = new Tasks(id, name, desc, dateCreated, priority, categoryT, dateCompleted, statusT);
                searchList.add(t);

            }
            return searchList;

        }catch(SQLException se){
            System.out.println(se + se.getMessage());
            return null;
        }
    }

    public static List<Tasks> searchByCategory(String category) {
        try(Connection c = DriverManager.getConnection(dbURI);
             PreparedStatement pst = c.prepareStatement("SELECT  rowid, * FROM todos WHERE category =? AND status =? ORDER BY category")){
            
            pst.setString(1, category);
            pst.setString(2, "INCOMPLETE");

            List<Tasks> searchList = new ArrayList<>();

            ResultSet rsCategory = pst.executeQuery();

            while(rsCategory.next()){
                int id = rsCategory.getInt("rowid");
                String name = rsCategory.getString("taskName");
                String desc = rsCategory.getString("description");
                Date dateCreated = new Date(rsCategory.getLong("dateCreated"));
                int priority = rsCategory.getInt("priority");
                String categoryDB = rsCategory.getString("category");
                Tasks.TasksCategory categoryT = Tasks.TasksCategory.valueOf(categoryDB);
                Date dateCompleted = new Date(rsCategory.getLong("dateCompleted"));
                String statusDB = rsCategory.getString("status");
                Tasks.TasksStatus statusT = Tasks.TasksStatus.valueOf(statusDB);

                Tasks t = new Tasks(id, name, desc, dateCreated, priority, categoryT, dateCompleted, statusT);
                searchList.add(t);

            }
            return searchList;
            
            
            
        }catch(SQLException se){
            System.out.println(se + se.getMessage());
            return null;
        }
        
        
    }

    public static void updateByTaskName(String taskName) {

        try(Connection c = DriverManager.getConnection(dbURI);
             PreparedStatement pst = c.prepareStatement("UPDATE todos SET status = ? WHERE taskName = ?")){

            pst.setString(1, "COMPLETED");
            pst.setString(2, taskName);

            pst.executeUpdate();



        }catch (SQLException se){
            System.out.println(se);
        }
    }

    public static void deleteTasks(String taskName) {

        try(Connection c= DriverManager.getConnection(dbURI);
             PreparedStatement pst = c.prepareStatement("DELETE FROM todos WHERE taskName= ?")){

            pst.setString(1, taskName);
            pst.executeUpdate();

        }catch(SQLException se){
            System.out.println(se);
        }
    }
}
