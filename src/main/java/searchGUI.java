import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton backToMainButton;

    protected DefaultListModel<Task> searchListModel;
    final ToDoListManager parentComponent;

    static final String ALL_TASKS = "Showing all incomplete task(s)";
    static final String NO_TASKS_FOUND = "No Matching task";
    static final String MATCHING_TASKS = "Matching Task(s)";

    searchGUI(ToDoListManager parentComponent){
        this.parentComponent = parentComponent;

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
        backToMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentComponent.setEnabled(true);
                searchGUI.this.dispose();
            }
        });


    }

    public void getList(List<Task> data, String msg) {
        searchListModel.clear();
        if(data != null){
            for(Task t : data){
                searchListModel.addElement(t);
            }
        }

        searchListDescriptionLabel.setText(msg);

    }
}
