import javax.swing.*;
import java.awt.*;
import java.util.List;

public class searchGUI extends JFrame {

    private JList <Task>  searchList;
    private JComboBox <Integer> searchByPriorityComboBox;
    private JComboBox  <String>  searchByCategoryComboBox;
    private JButton searchByPriority;
    private JButton searchByCategoryButton;
    private JLabel searchListDescriptionLabel;
    private JButton completedTaskButton;
    private JButton editButton;
    private JButton showAllTasks;
    private JPanel searchPanel;

    protected DefaultListModel<Task> searchListModel;

    searchGUI(final ToDoListManager parentComponent){
        setContentPane(searchPanel);
        pack();
        setVisible(true);
        parentComponent.setEnabled(false);
        setPreferredSize(new Dimension(500,500));


        //initialize the List Model for search JList
        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);
        //the program will show all incomplete tasks when it starts
        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        //get every tasks from the Database through the controller
        //List<Task> allData = controller.getAllTasks();
        //call the method to loop through the List of result
       // setListData(allData);
        //makesure the user can only do single selection
        searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addListener();


    }

    private void addListener() {


    }

    public void getList(List<Task> data) {
        searchListModel.clear();
        if(data != null){
            for(Task t : data){
                searchListModel.addElement(t);
            }
        }


    }
}
