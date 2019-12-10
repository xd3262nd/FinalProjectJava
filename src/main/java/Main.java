public class Main {


    //launches GUI
    static ToDoListManager todoGUI;

    public static void main(String[] args) {

        //store the db link as a string
        String databaseURI = DBConfig.dbURI;

        ListStore store = new ListStore(databaseURI);
        ListController controller = new ListController(store);
        todoGUI = new ToDoListManager(controller);

    }

    public static void quit(){
        todoGUI.dispose();
    }


}
