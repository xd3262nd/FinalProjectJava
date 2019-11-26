import java.util.Vector;

public class ListController {

    ListStore store;

    ListController (ListStore store){
        this.store = store;
    }

    public Vector<Vector> IncompleteData() {

        Vector<Vector> incompleteData = ListStore.getAllincompleteData();

        return incompleteData;

    }

    public Vector<Vector> CompletedData() {

        Vector<Vector> completedData = ListStore.getAllcompletedData();

        return completedData;


    }

    //need to update/refresh the todotable and completedtable

    //add new tasks into database

    //completed tasks button control

    //search by priority button

    //search by category button

    //edit description button





//closing
}
