import javax.swing.*;


import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    private JButton searchListButton;

    static final String ALL_TASKS = "Showing all incomplete task(s)";
    static final String NO_TASKS_FOUND = "No Matching task";
    static final String MATCHING_TASKS = "Matching Task(s)";

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


        //method for config and event listener
        configComboBox();
        addListener();
        configPopUpMenu();

        //initialize the List Model for search JList
        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);
        //the program will show all incomplete tasks when it starts
        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        //get every tasks from the Database through the controller
        List<Task> allData = controller.getAllTasks();
        //call the method to loop through the List of result
        setListData(allData);
        //makesure the user can only do single selection
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
        //set up the component for the popup menu on the JTable
        todoTable.setComponentPopupMenu(toDoClickMenu);
        completeTable.setComponentPopupMenu(completedClickMenu);

    }


    private void setUpTable() {
        //get the column names set up
        Vector colNames = controller.getColNames();

        //create a set of vector of vector to get the data for all the incomplete Data and completed Data
        Vector <Vector> incompletedData = controller.IncompleteData();

        Vector <Vector> completedData = controller.CompletedData();

        //Set up the JTable to make sure it is in default and is not editable
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

        //Setting up the JTable
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.setModel(todoModel);
        todoTable.setAutoCreateRowSorter(true);

        completeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completeTable.setModel(completedModel);
        completeTable.setAutoCreateRowSorter(true);

    }
    private void updateTable() {

        //refresh the Jtable with this method
        //To get the updated data

        Vector colNames = controller.getColNames();


        Vector <Vector> incompletedData = controller.IncompleteData();

        Vector <Vector> completedData = controller.CompletedData();

        todoModel.setDataVector(incompletedData, colNames);
        completedModel.setDataVector(completedData, colNames);

    }

    private void addListener() {
//
//        searchListButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                searchGUI searchAction = new searchGUI(ToDoListManager.this);
//            }
//        });

        priorityComboBox.addItemListener(e -> {
        });

        //this will be calling the controller to load all tasks and order by status first then the priority.
        //Then it will print out in the JLIST on searchPanel and change the description on the searchLabel
        showAllTasks.addActionListener(e -> allTasksAction());

        addButton.addActionListener(e -> addTaskAction());

        todoTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //obtained the codes from StackOverFlow
                //to make sure when the user right clicked on the JTable it will get the position of the action
                //instead of which row it is selected with left clicked
                int r = todoTable.rowAtPoint(e.getPoint());
                if(r>=0 && r<todoTable.getRowCount()){
                    todoTable.setRowSelectionInterval(r,r);
                }else{
                    todoTable.clearSelection();
                }

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
                //obtained the codes from StackOverFlow
                //to make sure when the user right clicked on the JTable it will get the position of the action
                //instead of which row it is selected with left clicked
                int r = completeTable.rowAtPoint(e.getPoint());
                if(r>=0 && r<completeTable.getRowCount()){
                    completeTable.setRowSelectionInterval(r,r);
                }else{
                    completeTable.clearSelection();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //listener for all the popup Menu Item
        //When the delete Menu Item is being selected
        deleteCompletedItem.addActionListener(e -> deleteCompleted());

        //when completed menu item (on ToDoTable) is being selected
        completedMenuItem.addActionListener(a -> completedSelected());

        //if delete(ToDoTable) menu item is being clicked
        deleteToDoItem.addActionListener(e -> deleteSelected());

        //when the get more details menu item (ToDoTable) is being clicked
        getMoreDetails.addActionListener(Details -> moreDetailsSelected());

        completedTaskButton.addActionListener(e -> completedTaskAction());

        searchByPriority.addActionListener(e -> prioritySearchAction());

        searchByCategoryButton.addActionListener(e -> categorySearchAction());

        editButton.addActionListener(e -> editTaskAction());

    }

    //methods from the event Listener

    private void allTasksAction() {
        //to get all tasks
        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Task> allData = controller.getAllTasks();
        setListData(allData);
    }

    private void editTaskAction() {
        //initialize the Task object
        Task t;

        if(searchList.getSelectedValue() == null){
            showMessageDialog("Please select one task from the table below to edit");
        }else{
            t = searchList.getSelectedValue();
            editTask(t);
        }
    }

    private void categorySearchAction() {
        if(searchByCategoryComboBox.getSelectedIndex()<0){
            //printing error message when there is no selection on the combo box

            showMessageDialog("Please select a category to  search for");
        }else{
            String categorySelected =searchByCategoryComboBox.getItemAt(searchByCategoryComboBox.getSelectedIndex());
            //search through the database to get the Tasks with the specific category
            List<Task> searchTask = controller.searchByCategory(categorySelected);
            //
            searchResultLookUp(searchTask);
        }
        searchByCategoryComboBox.setSelectedIndex(-1);
    }

    private void prioritySearchAction() {
        if(searchByPriorityComboBox.getSelectedIndex()<0){
            //printing error message when there is no selection on the combo box
            showMessageDialog("Please select a priority level");
        }else{
            //setting up the variable to search on the Database
            int p = searchByPriorityComboBox.getItemAt(searchByPriorityComboBox.getSelectedIndex());
            List<Task> searchTask = controller.searchByPriority(p);
            //calling the method to search for Task
            searchResultLookUp(searchTask);
        }
        searchByPriorityComboBox.setSelectedIndex(-1);
    }

    private void completedTaskAction() {
        searchList.setSelectedIndex(-1);
        //get the index for the selection
        int index = searchList.getSelectedIndex();

        //checking for selection
        boolean checkIndex = false;
        //if the index is not being selected
        if(index<0){
            showMessageDialog("No tasks being selected");
        }else{
            //change the validation when there is a selected on the JList
            checkIndex = true;
        }

        //when there is a selection
        if(checkIndex){
            //get the value
            Task value = searchList.getSelectedValue();
            //calling the controller to ge the Task by TaskID
            Task getTask = controller.searchByID(value.getTaskID());
            //setting up the completed date
            getTask.setDateCompleted(new Date());
            //set the status
            getTask.setStatus(Task.TaskStatus.COMPLETED);
            //update the selected task
            controller.updateTask(getTask);

            showMessageDialog("Task has Updated");
            //refresh the Table acoordingly
            updateTable();
            searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
            //Show the new list in the JList
            List<Task> allData = controller.getAllTasks();
            setListData(allData);

        }
    }

    private void addTaskAction() {
        //get the value to add as a new task
        String rawName = tasksNameText.getText();
        String rawDesc = descriptionText.getText();

        //validate if any of the input contains either null or empty
        if (rawDesc==null || rawDesc.isBlank() ||rawDesc.isEmpty()){
            showMessageDialog("Missing description");
        } else if (rawName ==null|| rawName.isBlank()||rawName.isEmpty()){
            showMessageDialog("Missing tasks name");
        } else if (priorityComboBox.getSelectedIndex()<0){
            showMessageDialog("Please select a priority level");
        } else if (categoryComboBox.getSelectedIndex()<0){
            showMessageDialog("Please assign a Category for your tasks");
        } else {
            //trim down the name and the description in case of any empty spaces before or after the text
            //assign each value into a variable name
            String name = rawName.trim();
            String desc = rawDesc.trim();
            Date dateCreated = new Date();
            int priority =priorityComboBox.getItemAt(priorityComboBox.getSelectedIndex());
            String categoryTask = categoryComboBox.getItemAt(categoryComboBox.getSelectedIndex());
            Task.TaskCategory category = Task.TaskCategory.valueOf(categoryTask);

            //added as a new Task object
            Task newTask = new Task(name, desc, dateCreated, priority, category);
            //then call upon the controller to execute code to add the new Task into the Database
            boolean validateName = controller.addTask(newTask);

            tasksNameText.setText("");
            descriptionText.setText("");
            priorityComboBox.setSelectedIndex(-1);
            categoryComboBox.setSelectedIndex(-1);
            //refresh the table
            if(validateName == false){
                detailsDialog("Duplicate Task Name \nNo Task being added", "Error Message", JOptionPane.ERROR_MESSAGE);
            }else{
                updateTable();
                detailsDialog("Added your task!", "Message", JOptionPane.INFORMATION_MESSAGE);
                searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                List<Task> allData = controller.getAllTasks();
                setListData(allData);
            }
        }
    }

    private void deleteCompleted() {

        if(completeTable.getSelectedRow()<0){
            //makesure the user select a row before right clicked
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );

        } else {
            //get the element where the user clicked on
            Vector data = completedModel.getDataVector().elementAt(completeTable.getSelectedRow());
            //get the selected ID Number on the index 0
            int selectedID = (int) data.get(0);
            String taskName = (String)data.get(1);
            //called on the controller to delete Task from the database
            controller.deleteTask(selectedID);
            //refresh the table
            updateTable();
            List<Task> allData = controller.getAllTasks();
            setListData(allData);
            //inform the user that it has been deleted
            detailsDialog("Successfully deleted " + taskName, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchResultLookUp(List<Task> results) {

        //if there is nothing in the result
        if(results==null||results.isEmpty()){
            searchListModel.clear();
            searchListDescriptionLabel.setText(ToDoListManager.NO_TASKS_FOUND);
        } else {
            //if there is results on the list
            searchListDescriptionLabel.setText(ToDoListManager.MATCHING_TASKS);
            //get the list printed on the JList
            setListData(results);
        }
    }

    private void editTask(Task t) {
        //get the new description
        String newDescription = showInputDialog("Enter text for your new description");
        //change the description on the Task Object
        t.setDescription(newDescription);
        //update the Task object on Database too
        controller.updateTask(t);

        //refresh the jtable and jlist
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
            //get the task details from the database through the controller
            Task t = controller.searchByID(selectedID);
            //store the description to print out the details later
            String statement = t.getDescription();
            //print out for the result/details of the Task
            detailsDialog(statement, "Description for " + t.getName(), JOptionPane.INFORMATION_MESSAGE);
        }


    }
    private void deleteSelected() {

        if(todoTable.getSelectedRow()<0){
            //error message
            detailsDialog("Please select a row before proceed", "Error",JOptionPane.ERROR_MESSAGE );
        }else{
            //get the data from the selected row
            Vector data = todoModel.getDataVector().elementAt(todoTable.getSelectedRow());

            int selectedID = (int) data.get(0);
            String taskName = (String) data.get(1);
            //delete the task on Database through controller
            controller.deleteTask(selectedID);
            //refresh the Jtable and JList
            updateTable();
            List<Task> allData = controller.getAllTasks();
            setListData(allData);
            //inform the user the task has been deleted
            detailsDialog("Successfully deleted " + taskName, "Message", JOptionPane.INFORMATION_MESSAGE);
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
        //getting the location and Task information of the selected value
        Vector data = todoModel.getDataVector().elementAt(todoTable.getSelectedRow());

        String taskName = (String) data.get(1);
        int selectedID = (int) data.get(0);
        //search the Task on the database
        Task getTask = controller.searchByID(selectedID);

        getTask.setDateCompleted(new Date());

        getTask.setStatus(Task.TaskStatus.COMPLETED);
        //update the task on JList and JTable
        controller.updateTask(getTask);
        updateTable();
        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Task> allData = controller.getAllTasks();
        setListData(allData);
        //informing the user about the update
        detailsDialog("Updated this " + taskName +" as completed", "Message", JOptionPane.INFORMATION_MESSAGE);

    }

    void setListData(List<Task> data) {
        //set up to present the information into the JList with the Task data available
//        searchListModel.clear();
//        if(data != null){
//            for(Task t : data){
//                searchListModel.addElement(t);
//            }
//        }

        searchGUI searchResult = new searchGUI(ToDoListManager.this);
        searchResult.getList(data);


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
        //This will need to makesure the user clicked on the priority comboBox
        priorityComboBox.setSelectedIndex(-1);

        categoryComboBox.setModel(categoryListModel);
        categoryComboBox.setSelectedIndex(-1);

        searchByPriorityComboBox.setModel(priorityLModel);
        //This will need to makesure the user clicked on the priority comboBox
        searchByPriorityComboBox.setSelectedIndex(-1);

        searchByCategoryComboBox.setModel(categoryLModel);
        searchByCategoryComboBox.setSelectedIndex(-1);

        quitButton.addActionListener(e -> quitProgram());


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
