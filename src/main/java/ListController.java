import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ListController {

    ListStore store;

    ListController (ListStore store){
        this.store = store;
    }

    protected Vector<Vector> IncompleteData() {

        return ListStore.AllIncompleteData();

    }

    protected Vector <Vector> CompletedData() {

        return ListStore.AllCompletedData();


    }

    protected List<Task> getAllTasks() {
        return ListStore.getAllData();

    }

    protected void quitProgram() {
       Main.quit();
    }

    public Vector getColNames() {

        return ListStore.getColumnNames();
    }

    public boolean addTask(Task newTask) {
        try{
            ListStore.add(newTask);
            return true;
        }catch(SQLException s){
            return false;
        }
    }

    protected Task searchByID(int id) {
        return ListStore.getTaskInfo(id);
    }

    protected void updateTask(Task selectedTask) {

        ListStore.updateTask(selectedTask);
    }

    protected List<Task> searchByPriority(int p) {
        return ListStore.searchByPriority(p);
    }

    protected List<Task> searchByCategory(String category) {


        return ListStore.searchByCategory(category);
    }


    public void deleteTask(int selectedID) {
        ListStore.deleteTask(selectedID);
    }

    public Task getDetailsByID(int selectedID) {
        return ListStore.getDetailsByID(selectedID);
    }


//closing
}
