import javax.swing.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class ToDoListManager extends JFrame{


    private JPanel mainPanel;

    private JPanel addTaskPanel;
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
    private JButton completedTaskButton;
    private JButton showAllTasks;
    private JLabel toDoLabel;
    private JLabel completedList;
    private JComboBox <Integer> searchByPriorityComboBox;
    private JButton searchByPriority;
    private JButton searchByCategoryButton;
    private JPanel searchPanel;
    private JComboBox <String> searchByCategoryComboBox;
    private JList <Task> searchList;
    private JLabel searchListDescriptionLabel;
    private JButton quitButton;
    private JPanel controlPanel;
    private JButton editButton;
    private JTable todoTable;
    private JTable completeTable;

    static final String ALL_TASKS = "Showing all incomplete tasks";
    static final String NO_TASKS_FOUND = "No Matching tasks";
    static final String MATCHING_TASKS = "Matching Incomplete Task";




    protected DefaultListModel<Task> searchListModel;
    DefaultComboBoxModel<Integer> priorityListModel;
    DefaultComboBoxModel <String> categoryListModel;

    DefaultComboBoxModel<Integer> priorityLModel;
    DefaultComboBoxModel<String> categoryLModel;


    protected DefaultTableModel todoModel;
    protected DefaultTableModel completedModel;

    //Menu Items for popUpMenu
    //item in the menu
    JMenuItem completedMenuItem = new JMenuItem("Completed");
    JMenuItem deleteToDoItem = new JMenuItem("Delete"); //Add that you are about to delete this items from the list forever warning
    JMenuItem getMoreDetails = new JMenuItem("Show Description");

    JMenuItem deleteCompletedItem = new JMenuItem("Delete");



    // TODO Use these instead of your own Strings. The tests expect you to use these constants

    private ListController controller;

    ToDoListManager(ListController controller){
        this.controller = controller;

        setTitle("To-Do Task Manager");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800,800));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        configComboBox();
        addListener();
        configPopUpMenu();

        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);

        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Task> allData = controller.getAllTasks();
        setListData(allData);

        searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        setUpTable();

    }

    private void configPopUpMenu() {

//create the popup menu
        JPopupMenu toDoClickMenu = new JPopupMenu();
        JPopupMenu completedClickMenu = new JPopupMenu();




//add elements into the clickMenu
        toDoClickMenu.add(completedMenuItem);
        toDoClickMenu.add(deleteToDoItem);
        toDoClickMenu.add(new JSeparator());
        toDoClickMenu.add(getMoreDetails);

        completedClickMenu.add(deleteCompletedItem);

        todoTable.setComponentPopupMenu(toDoClickMenu);
        completeTable.setComponentPopupMenu(completedClickMenu);



    }


    private void setUpTable() {


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

        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.setModel(todoModel);
        todoTable.setAutoCreateRowSorter(true);

        completeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completeTable.setModel(completedModel);
        completeTable.setAutoCreateRowSorter(true);

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
                List<Task> allData = controller.getAllTasks();
                setListData(allData);
            }
        });


        //ToDO this will do on adding the tasks with the new tasks constructor and need to add into DB
        //Need to have a confirmation messgae that it is added
        //run thru the controller to load the JTABLE??
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String rawName = tasksNameText.getText();
                String rawDesc = descriptionText.getText();

                if(rawDesc==null || rawDesc.isBlank() ||rawDesc.isEmpty()){
                    showMessageDialog("Missing description");
                }else if(rawName ==null|| rawName.isBlank()||rawName.isEmpty()){
                    showMessageDialog("Missing tasks name");
                }else if(priorityComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a priority level");
                }else if(categoryComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please assign a Category for your tasks");
                }else{
                    String name = rawName.trim();
                    String desc = rawDesc.trim();
                    Date dateCreated = new Date();
                    int priority =priorityComboBox.getItemAt(priorityComboBox.getSelectedIndex());
                    String categoryTask = categoryComboBox.getItemAt(categoryComboBox.getSelectedIndex());
                    Task.TaskCategory cat = Task.TaskCategory.valueOf(categoryTask);
                    Task newTask = new Task(name,desc,dateCreated, priority,cat );

                    controller.addTask(newTask);

                    tasksNameText.setText("");
                    descriptionText.setText("");
                    priorityComboBox.setSelectedIndex(-1);
                    categoryComboBox.setSelectedIndex(-1);

                    updateTable();
                    searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                    List<Task> allData = controller.getAllTasks();
                    setListData(allData);

                }

            }
        });


        //To add the mouse action listener to makesure can choose the selection when mouse is being clicked
        todoTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        completeTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        deleteCompletedItem.addActionListener(e -> deleteCompleted());


        //when completed menu item is being selected
        completedMenuItem.addActionListener(a -> completedSelected());

        //if delete is being clicked
        deleteToDoItem.addActionListener(e -> deleteSelected());

        getMoreDetails.addActionListener(Details -> moreDetailsSelected());



        completedTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                searchList.setSelectedIndex(-1);
                //get the index for the selection
                int index = searchList.getSelectedIndex();

                //checking for selection
                boolean checkIndex = false;

                if(index<0){
                    showMessageDialog("No tasks being selected");
                }else{
                    //change the validation
                    checkIndex = true;
                }

                //when there is a selection
                if(checkIndex ==true){
                    //get the tasks value
                    Task value = searchList.getSelectedValue();

                    Task getTask = controller.searchByID(value.getTaskID());

                    getTask.setDateCompleted(new Date());

                    getTask.setStatus(Task.TaskStatus.COMPLETED);

                    controller.updateTask(getTask);

                    showMessageDialog("Task Updated");

                    updateTable();
                    searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                    List<Task> allData = controller.getAllTasks();
                    setListData(allData);

                }
            }

        });

        searchByPriority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(searchByPriorityComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a priority level");
                }else{
                    int p = searchByPriorityComboBox.getItemAt(searchByPriorityComboBox.getSelectedIndex());
                    List<Task> searchTask;
                    searchTask = controller.searchByPriority(p);
                    PrioritySearch(searchTask);
                }
                searchByPriorityComboBox.setSelectedIndex(-1);
            }
        });

        searchByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(searchByCategoryComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a category to  search for");
                }else{
                    String categorySelected =searchByCategoryComboBox.getItemAt(searchByCategoryComboBox.getSelectedIndex());

                    List<Task> searchTask = new ArrayList<>();

                    searchTask = controller.searchByCategory(categorySelected);

                    searchCategoryResults(searchTask);
                }
                searchByCategoryComboBox.setSelectedIndex(-1);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //initialize the Task object
                Task t;

                if(searchList.getSelectedValue() == null){
                    showMessageDialog("Please select one task from the table below to edit");
                }else{
                    t = searchList.getSelectedValue();
                    editTask(t);
                }

            }


        });


    }

    private void deleteCompleted() {

        if(completeTable.getSelectedRow()<0){
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );

        }else{
            Vector data = completedModel.getDataVector().elementAt(completeTable.getSelectedRow());
            int selectedID = (int) data.get(0);

            controller.deleteTask(selectedID);
            updateTable();
            List<Task> allData = controller.getAllTasks();
            setListData(allData);

            showMessageDialog("Deleted Selected Task");
        }
    }
    private void PrioritySearch(List<Task> results) {

        if(results==null||results.isEmpty()){
            searchListModel.clear();
            searchListDescriptionLabel.setText(ToDoListManager.NO_TASKS_FOUND);
        }else{

            searchListDescriptionLabel.setText(ToDoListManager.MATCHING_TASKS);
            setListData(results);
        }
    }

    private void searchCategoryResults(List<Task> search) {

        if(search==null||search.isEmpty()){
            searchListModel.clear();
            searchListDescriptionLabel.setText(ToDoListManager.NO_TASKS_FOUND);
        }else{

            searchListDescriptionLabel.setText(ToDoListManager.MATCHING_TASKS);
            setListData(search);
        }

    }

    private void editTask(Task t) {
        String newDescription = showInputDialog("Enter text for your new description");
        t.setDescription(newDescription);
        controller.updateTask(t);
        updateTable();

        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Task> allData = controller.getAllTasks();
        setListData(allData);

    }

    private void moreDetailsSelected() {

        if(todoTable.getSelectedRow()<0){
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );
        }else{
            //get the date from the selected row
            Vector data = todoModel.getDataVector().elementAt(todoTable.getSelectedRow());

            int selectedID = (int) data.get(0);

            Task t = controller.getDetailsByID(selectedID);
            String statement = t.getDescription();

            detailsDialog(statement, "Description for " + t.getName(), JOptionPane.INFORMATION_MESSAGE);
        }


    }
    private void deleteSelected() {

        if(todoTable.getSelectedRow()<0){
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );
        }else{
            //get the data from the selected row
            Vector data = todoModel.getDataVector().elementAt(todoTable.getSelectedRow());

            int selectedID = (int) data.get(0);

            controller.deleteTask(selectedID);
            updateTable();
            List<Task> allData = controller.getAllTasks();
            setListData(allData);

            showMessageDialog("Deleted Selected Task");
        }

    }

    private void completedSelected(){

        //get the data from the selected row
        if(todoTable.getSelectedRow()<0){
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );
        }else{
            getSelectedValue();
        }
    }
    private void getSelectedValue() {
        Vector data = todoModel.getDataVector().elementAt(todoTable.getSelectedRow());

        int selectedID = (int) data.get(0);

        Task getTask = controller.searchByID(selectedID);

        getTask.setDateCompleted(new Date());

        getTask.setStatus(Task.TaskStatus.COMPLETED);

        controller.updateTask(getTask);

        updateTable();

        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Task> allData = controller.getAllTasks();
        setListData(allData);

        showMessageDialog("Updated Selected Task");
    }

    void setListData(List<Task> data) {

        searchListModel.clear();
        if(data != null){
            for(Task t : data){
                searchListModel.addElement(t);
            }
        }
    }

    private void configComboBox() {

        int [] index = {1,2,3,4,5};
        String [] categoryList = {"PERSONAL", "WORK"};

        priorityListModel = new DefaultComboBoxModel<>();
        categoryListModel = new DefaultComboBoxModel<>();

        priorityLModel = new DefaultComboBoxModel<>();
        categoryLModel = new DefaultComboBoxModel<>();

        for (int value : index) {

            priorityLModel.addElement(value);
        }

        for (String s : categoryList) {
            categoryLModel.addElement(s);
        }


        for (int value : index) {

            priorityListModel.addElement(value);
        }

        for (String s : categoryList) {
            categoryListModel.addElement(s);
        }


        priorityComboBox.setModel(priorityListModel);
        //TODO This will need to makesure the user clicked on the priority comboBox
        priorityComboBox.setSelectedIndex(-1);

        categoryComboBox.setModel(categoryListModel);
        categoryComboBox.setSelectedIndex(-1);

        searchByPriorityComboBox.setModel(priorityLModel);
        //TODO This will need to makesure the user clicked on the priority comboBox
        searchByPriorityComboBox.setSelectedIndex(-1);

        searchByCategoryComboBox.setModel(categoryLModel);
        searchByCategoryComboBox.setSelectedIndex(-1);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitProgram();
            }
        });


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


    protected void detailsDialog(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);


    }

}
