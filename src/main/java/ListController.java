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


    //need to update/refresh the todotable and completedtable

    //add new tasks into database

    //completed tasks button control

    //search by priority button

    //search by category button

    //edit description button





//closing
}
