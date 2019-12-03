import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ListController {

    ListStore store;

    ListController (ListStore store){
        this.store = store;
    }

    protected Vector<Vector> IncompleteData() {

        return ListStore.getAllincompleteData();

    }

    protected Vector <Vector> CompletedData() {

        return ListStore.getAllcompletedData();


    }

    protected List<Tasks> loadAllTasksFromStore() {
        return ListStore.getAllData();

    }

    protected void quitProgram() {
       Main.quit();
    }

    public Vector getColNames() {

        return ListStore.getColumnNames();
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
        return ListStore.getTasksInfo(id);
    }

    protected void updateTasks(Tasks selectedTask) {

        ListStore.updateTasks(selectedTask);
    }

    protected List<Tasks> searchByPriority(int p) {
        return ListStore.searchByPriority(p);
    }

    protected List<Tasks> searchByCategory(String category) {


        return ListStore.searchByCategory(category);
    }


    public void deleteTasks(int selectedID) {
        ListStore.deleteTasks(selectedID);
    }

    public Tasks getDetailsByID(int selectedID) {
        return ListStore.getDetailsBYID(selectedID);
    }


//closing
}
