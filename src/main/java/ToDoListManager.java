import javax.swing.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class ToDoListManager extends JFrame{
    private JPanel mainPanel;

    private JPanel addTasksPanel;
    private JTextField tasksNameText;
    private JTextField descriptionText;
    private JComboBox <Integer>priorityComboBox;
    private JComboBox <String> categoryComboBox;
    private JButton addButton;
    private JLabel tasksNameLabel;
    private JLabel descriptionLabel;
    private JLabel priorityLabel;
    private JLabel categoryLabel;

    private JPanel toDoListPanel;
    private JButton completedTaksButton;
    private JButton showAllTasks;
    private JLabel toDoLabel;
    private JLabel completedList;
    private JComboBox <Integer> searchByPriorityComboBox;
    private JButton searchByPriority;
    private JButton searchByCategoryButton;
    private JPanel searchPanel;
    private JComboBox <String> searchByCategoryComboBox;
    private JList <Tasks> searchList;
    private JLabel searchListDescriptionLabel;
    private JButton quitButton;
    private JPanel controlPanel;
    private JButton editButton;
    private JButton refreshButton;
    private JTable todoTable;
    private JTable completeTable;

    static final String ALL_TASKS = "Showing all incomplete tasks";




    protected DefaultListModel<Tasks> searchListModel;
    DefaultComboBoxModel<Integer> priorityListModel;
    DefaultComboBoxModel <String> categoryListModel;
    protected DefaultTableModel todoModel;
    protected DefaultTableModel completedModel;
    //need to setmodel for the default table model


    // TODO Use these instead of your own Strings. The tests expect you to use these constants

    private ListController controller;

    ToDoListManager(ListController controller){
        this.controller = controller;

        setTitle("To-Do Tasks Manager");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        configComboBox();
        addListener();



        //todo create listmodel for the taskslist

        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);

        //TODO table set up
        //setup table
        //update table here
//        todoModel = new DefaultTableModel();
//        completedModel = new DefaultTableModel();

        setUpTable(); //TODO need to make sure the user can not click on the table
        //TODO this should be in the refresh button
        //updateTable();



        //TODO need to call controller for load all tasks when the program start here //do not need to do this
        //Will need to return Tasks List from the controller load all tasks
        //and call method setListData
        //  List<Ticket> listData = controller.loadOpenTicketsFromTicketStore();
        //  setListData(listData);
        //ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);







    }


    private void setUpTable() {

//        Vector <String> columnNames = new Vector<>();
//        columnNames.add("Tasks");
//        columnNames.add("Description");
//        columnNames.add("Priority");
//        columnNames.add("Category");

        Vector colNames = controller.getColNames();

        Vector <Vector> incompletedData = controller.IncompleteData();

        Vector <Vector> completedData = controller.CompletedData();

        todoModel = new DefaultTableModel(incompletedData, colNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        completedModel = new DefaultTableModel(completedData, colNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //TODO not sure if needed this one
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.setModel(todoModel);

        completeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completeTable.setModel(completedModel);

    }
    private void updateTable() {

        //TODO refresh button will call on this method
        Vector colNames = controller.getColNames();

        Vector <Vector> incompletedData = controller.IncompleteData();

        Vector <Vector> completedData = controller.CompletedData();

        todoModel.setDataVector(incompletedData, colNames);
        completedModel.setDataVector(completedData, colNames);

    }


    private void addListener() {

        priorityComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        });


        //TODO this will be calling the controller to load all tasks and order by status first then the priority.
        //Then it will print out in the JLIST on searchPanel and change the description on the search Panel
        //button listener
        showAllTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                List<Tasks> allData = controller.loadAllTasksFromStore();
                setListData(allData);



            }
        });


        //ToDO this will do on adding the tasks with the new tasks constructor and need to add into DB
        //Need to have a confirmation messgae that it is added
        //run thru the controller to load the JTABLE??
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



    }

    void setListData(List<Tasks> data) {

        searchListModel.clear();
        if(data != null){
            for(Tasks t : data){
                searchListModel.addElement(t);
            }
        }
    }

    private void configComboBox() {

        int [] index = {1,2,3,4,5};
        String [] categoryList = {"PERSONAL", "WORK"};

        priorityListModel = new DefaultComboBoxModel<>();
        categoryListModel = new DefaultComboBoxModel<>();


        for(int i=0; i<index.length; i++){

            priorityListModel.addElement(index[i]);
        }

        for(int j=0; j<categoryList.length;j++){
            categoryListModel.addElement(categoryList[j]);
        }


        priorityComboBox.setModel(priorityListModel);
        //TODO This will need to makesure the user clicked on the priority comboBox
        priorityComboBox.setSelectedIndex(-1);

        categoryComboBox.setModel(categoryListModel);
        categoryComboBox.setSelectedIndex(-1);

        searchByPriorityComboBox.setModel(priorityListModel);
        //TODO This will need to makesure the user clicked on the priority comboBox
        searchByPriorityComboBox.setSelectedIndex(-1);

        searchByCategoryComboBox.setModel(categoryListModel);
        searchByCategoryComboBox.setSelectedIndex(-1);


    }


    protected void quitProgram(){
        controller.quitProgram();
    }

    protected void showMessageDialog(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    protected String showInputDialog(String question){
        return JOptionPane.showInputDialog(this, question);
    }

}
