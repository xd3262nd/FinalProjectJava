import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ListController {

    ListStore store;

    ListController (ListStore store){
        this.store = store;
    }

    protected Vector<Vector> IncompleteData() {

        Vector<Vector> incompleteData = ListStore.getAllincompleteData();

        return incompleteData;

    }

    protected Vector <Vector> CompletedData() {

        Vector completedData = ListStore.getAllcompletedData();

        return completedData;


    }

    protected List<Tasks> loadAllTasksFromStore() {
        List<Tasks> allTasks = ListStore.getAllData();
        return allTasks;

    }

    protected void quitProgram() {
       Main.quit();
    }

    public Vector getColNames() {
        Vector colNames = ListStore.getColumnNames();
        return colNames;
    }

    public boolean addTasks(Tasks newTasks) {
        try{
            ListStore.add(newTasks);
            return true;
        }catch(SQLException s){
            return false;
        }
    }

    protected Tasks searchByID(int id) {
        Tasks t = ListStore.getTasksInfo(id);
        return t;
    }

    protected void updateTasks(Tasks selectedTask) {
        ListStore.updateTasks(selectedTask);
    }

    protected List<Tasks> searchByPriority(int p) {
        List<Tasks> tasksResult = ListStore.searchByPriority(p);
        return tasksResult;
    }

    protected List<Tasks> searchByCategory(String category) {
        List<Tasks> tasksResult = ListStore.searchByCategory(category);


        return tasksResult;
    }


    //need to update/refresh the todotable and completedtable

    //add new tasks into database

    //completed tasks button control

    //search by priority button

    //search by category button

    //edit description button





//closing
}
