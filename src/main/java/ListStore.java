import java.sql.*;
import java.util.*;
import java.util.Date;

public class ListStore {

    private static String dbURI;


    ListStore(String databaseURI){

        dbURI = databaseURI;
        //this will run when the program started
        try(Connection conn = DriverManager.getConnection(databaseURI);
            Statement stat = conn.createStatement()){
            //TODO google how to fix this to makesure is for the sqlite
            String createTable = "CREATE Table if not exists todos (taskName TEXT NOT NULL UNIQUE, description TEXT NOT NULL, priority INTEGER NOT NULL CHECK ( priority>=1 and priority<=5 ), category TEXT NOT Null Check(category = 'PERSONAL' or category ='WORK'), dateCreated INTEGER, dateCompleted INTEGER, status TEXT NOT NULL Check ( status = 'COMPLETED' or status='INCOMPLETE' ));";

            stat.executeUpdate(createTable);

        }catch (SQLException s){
            System.out.println(s);
        }
    }


    public static Vector<Vector> AllIncompleteData() {
        //get data to be printed into the JTable for incomplete data
        String sqlQuery = "SELECT rowid,* FROM todos WHERE status='INCOMPLETE'";

        try(Connection conn = DriverManager.getConnection(dbURI);
             Statement stat = conn.createStatement()){

            ResultSet rsIncomData = stat.executeQuery(sqlQuery);
            Vector<Vector> vectors = new Vector<>();

            while(rsIncomData.next()){
                int id = rsIncomData.getInt("rowid");
                String name = rsIncomData.getString("taskName");
                int prio = rsIncomData.getInt("priority");
                String category = rsIncomData.getString("category");

                Vector v = new Vector();
                v.add(id);
                v.add(name);
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

    public static Vector<Vector> AllCompletedData() {

        String sqlQuery = "SELECT rowid,* FROM todos WHERE status='COMPLETED'";

        try(Connection conn = DriverManager.getConnection(dbURI);
            Statement stat = conn.createStatement()){
            ResultSet rsIncomData = stat.executeQuery(sqlQuery);

            Vector<Vector> vectors = new Vector<>();

            while(rsIncomData.next()){
                String name = rsIncomData.getString("taskName");
                int id = rsIncomData.getInt("rowid");
                int prio = rsIncomData.getInt("priority");
                String category = rsIncomData.getString("category");

                Vector v = new Vector();
                v.add(id);
                v.add(name);
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

    public static List<Task> getAllData() {

        List<Task> allRecords = new ArrayList<>();

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
                Task.TaskCategory cat = Task.TaskCategory.valueOf(category);

                String status = rsall.getString("status");
                Task.TaskStatus statusTask = Task.TaskStatus.valueOf(status);

                Date dateCreated = new Date(rsall.getLong("dateCreated"));

                Date dateCompleted = new Date();

                if(rsall.getLong("dateCompleted")==0){
                    dateCompleted=null;
                }else{
                    dateCompleted =new Date(rsall.getLong("dateCompleted"));
                }

                Task task = new Task(id,name, desc, dateCreated, prio, cat, dateCompleted, statusTask);

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

        columnNames.add("TaskID");
        columnNames.add("Task");
        columnNames.add("Priority");
        columnNames.add("Category");
        return columnNames;
    }

    public static void add(Task newTask) throws SQLException{

        Connection conn = DriverManager.getConnection(dbURI);
        PreparedStatement pst = conn.prepareStatement("insert into todos values(?,?,?,?,?,?,?)");

        String name = newTask.getName();
        String desc = newTask.getDescription();
        int priority = newTask.getPriority();
        String category = String.valueOf(newTask.getCategory());

        Long dateCreatedInt = newTask.getDateCreated().getTime();

        String status = String.valueOf(newTask.getStatus());

        pst.setString(1, name);
        pst.setString(2, desc);
        pst.setInt(3, priority);
        pst.setString(4, category);
        pst.setLong(5, dateCreatedInt);
        pst.setString(7,status);
        if(newTask.getDateCompleted()==null){
            pst.setLong(6,0);
        }else{
            Long dateCompletedInt = newTask.getDateCompleted().getTime();
            pst.setLong(6,dateCompletedInt);
        }
        pst.execute();

        Statement s = conn.createStatement();
        ResultSet generateID = s.getGeneratedKeys();

        while(generateID.next()){
            int rowID = generateID.getInt(1);
            newTask.setTaskID(rowID);
        }
    }

    public static Task getTaskInfoByID(int id) {

        try(Connection c = DriverManager.getConnection(dbURI);
             PreparedStatement ps = c.prepareStatement("SELECT rowid,* FROM todos WHERE rowid = ?")){

            ResultSet rs;

            ps.setInt(1, id);

            rs=ps.executeQuery();

            Task t = null;
            if(rs.next()){

                String name = rs.getString("taskName");
                String desc = rs.getString("description");
                Date dateCreated = new Date(rs.getLong("dateCreated"));
                int priority = rs.getInt("priority");
                String categoryDB = rs.getString("category");
                Task.TaskCategory categoryT = Task.TaskCategory.valueOf(categoryDB);
                Date dateCompleted = new Date(rs.getLong("dateCompleted"));
                String statusDB = rs.getString("status");
                Task.TaskStatus statusT = Task.TaskStatus.valueOf(statusDB);

                t = new Task(id, name, desc, dateCreated, priority, categoryT, dateCompleted, statusT);

            }
            return t;

        }catch(SQLException s){
            System.out.println(s.getMessage()+s);
            return null;
        }
    }



    public static void updateTask(Task task) {

        try(Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement pst = conn.prepareStatement("UPDATE todos SET taskName =?, description =?, dateCreated=?, priority=?, category=?, dateCompleted=?, status=? WHERE rowid =?")){

            int id = task.getTaskID();
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

    public static List<Task> searchByPriority(int p) {

        try(Connection c = DriverManager.getConnection(dbURI);
            PreparedStatement pst = c.prepareStatement("SELECT rowid,* FROM todos WHERE priority= ? AND status = ? ORDER BY category")){

            pst.setInt(1, p);
            pst.setString(2, "INCOMPLETE");

            List<Task> searchList = new ArrayList<>();

            ResultSet rsP = pst.executeQuery();

            while(rsP.next()){
                int id = rsP.getInt("rowid");
                String name = rsP.getString("taskName");
                String desc = rsP.getString("description");
                Date dateCreated = new Date(rsP.getLong("dateCreated"));
                int priority = rsP.getInt("priority");
                String categoryDB = rsP.getString("category");
                Task.TaskCategory categoryT = Task.TaskCategory.valueOf(categoryDB);
                Date dateCompleted = new Date(rsP.getLong("dateCompleted"));
                String statusDB = rsP.getString("status");
                Task.TaskStatus statusT = Task.TaskStatus.valueOf(statusDB);

                Task t = new Task(id, name, desc, dateCreated, priority, categoryT, dateCompleted, statusT);
                searchList.add(t);

            }
            return searchList;

        }catch(SQLException se){
            System.out.println(se + se.getMessage());
            return null;
        }
    }

    public static List<Task> searchByCategory(String category) {
        try(Connection c = DriverManager.getConnection(dbURI);
             PreparedStatement pst = c.prepareStatement("SELECT  rowid, * FROM todos WHERE category =? AND status =? ORDER BY category")){
            
            pst.setString(1, category);
            pst.setString(2, "INCOMPLETE");

            List<Task> searchList = new ArrayList<>();

            ResultSet rsCategory = pst.executeQuery();

            while(rsCategory.next()){
                int id = rsCategory.getInt("rowid");
                String name = rsCategory.getString("taskName");
                String desc = rsCategory.getString("description");
                Date dateCreated = new Date(rsCategory.getLong("dateCreated"));
                int priority = rsCategory.getInt("priority");
                String categoryDB = rsCategory.getString("category");
                Task.TaskCategory categoryT = Task.TaskCategory.valueOf(categoryDB);
                Date dateCompleted = new Date(rsCategory.getLong("dateCompleted"));
                String statusDB = rsCategory.getString("status");
                Task.TaskStatus statusT = Task.TaskStatus.valueOf(statusDB);

                Task t = new Task(id, name, desc, dateCreated, priority, categoryT, dateCompleted, statusT);
                searchList.add(t);

            }
            return searchList;
            
            
            
        }catch(SQLException se){
            System.out.println(se + se.getMessage());
            return null;
        }

    }


    public static void deleteTask(int ID) {
        try(Connection c= DriverManager.getConnection(dbURI);
            PreparedStatement pst = c.prepareStatement("DELETE FROM todos WHERE rowid= ?")){

            pst.setInt(1, ID);
            pst.executeUpdate();

        }catch(SQLException se){
            System.out.println(se);
        }

    }


}
