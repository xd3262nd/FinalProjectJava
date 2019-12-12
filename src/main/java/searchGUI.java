import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class searchGUI extends JFrame {

    private JList <Task>  searchList;
    private JLabel searchListDescriptionLabel;
    private JPanel searchPanel;
    private JButton backToMainButton;
//    private JLabel tasksNameLabel;
//    private JLabel descriptionLabel;
//    private JLabel priorityLabel;
//    private JLabel categoryLabel;

    protected DefaultListModel<Task> searchListModel;
    final ToDoListManager parentComponent;


    searchGUI(ToDoListManager parentComponent){
        this.parentComponent = parentComponent;

        setTitle("Search List");

        setContentPane(searchPanel);
        pack();
        setVisible(true);
        parentComponent.setEnabled(false);
        setPreferredSize(new Dimension(1000,500));
        setLocationRelativeTo(null);


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
        backToMainButton.addActionListener(e -> {
            parentComponent.setEnabled(true);
            searchGUI.this.dispose();
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentComponent.setEnabled(true);
                searchGUI.this.dispose();
                super.windowClosing(e);
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
