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
    static final String NO_TASKS_FOUND = "No Matching tasks";
    static final String MATCHING_TASKS = "Matching Incomplete Tasks";




    protected DefaultListModel<Tasks> searchListModel;
    DefaultComboBoxModel<Integer> priorityListModel;
    DefaultComboBoxModel <String> categoryListModel;

    DefaultComboBoxModel<Integer> priorityLModel;
    DefaultComboBoxModel<String> categoryLModel;


    protected DefaultTableModel todoModel;
    protected DefaultTableModel completedModel;



    // TODO Use these instead of your own Strings. The tests expect you to use these constants

    private ListController controller;

    ToDoListManager(ListController controller){
        this.controller = controller;

        setTitle("To-Do Tasks Manager");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800,800));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        configComboBox();
        addListener();



        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);

        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        List<Tasks> allData = controller.loadAllTasksFromStore();
        setListData(allData);

        searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        setUpTable();

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
                boolean checkValidation;
                String rawName = tasksNameText.getText();
                String desc = descriptionText.getText();
//                    showMessageDialog("Missing Name", "Enter name", JOptionPane.ERROR_MESSAGE);
                // boolean checkIndex = false;
                //                String pText = newPlaceNameTextField.getText();
                //
                //                //when the JText is not empty
                //                if(!pText.isBlank()){
                //                    //make sure it gets trim off the blank space
                //                    //trim out the blank space after text
                //                    String placeText = pText.trim();
                //
                //                    //check to see if the Model List is empty
                //                    //so we can add the text into the ModelList
                //                    if(!(placeText ==null) && wishListModel.isEmpty() ){
                //                        wishListModel.addElement(placeText);
                //                        newPlaceNameTextField.setText("");


                if(desc==null || desc.isBlank() ||desc.isEmpty()){
                    showMessageDialog("Missing description");
                }else if(rawName ==null|| rawName.isBlank()||rawName.isEmpty()){
                    showMessageDialog("Missing tasks name");
                }else if(priorityComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a priority level");
                }else if(categoryComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please assign a Category for your tasks");
                }else{
                    String name = rawName.trim();
                    Date dateCreated = new Date();
                    int priority =priorityComboBox.getItemAt(priorityComboBox.getSelectedIndex());
                    String categoryTasks = categoryComboBox.getItemAt(categoryComboBox.getSelectedIndex());
                    Tasks.TasksCategory cat = Tasks.TasksCategory.valueOf(categoryTasks);
                    Tasks newTasks = new Tasks(name,desc,dateCreated, priority,cat );

                    controller.addTasks(newTasks);

                    tasksNameText.setText("");
                    descriptionText.setText("");
                    priorityComboBox.setSelectedIndex(-1);
                    categoryComboBox.setSelectedIndex(-1);

                    updateTable();
                    searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                    List<Tasks> allData = controller.loadAllTasksFromStore();
                    setListData(allData);

                }

            }
        });

//create the popup menu
        JPopupMenu tableClickMenu = new JPopupMenu();
//item in the menu
        JMenuItem completedMenuItem = new JMenuItem("Completed"); //maybe deleting this one to make it as completed instead
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        //TODO maybe will add completed this task in the table to remove the JButton


        tableClickMenu.add(completedMenuItem);
        tableClickMenu.add(deleteMenuItem);

        todoTable.setComponentPopupMenu(tableClickMenu);

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

        //when completed menu item is being selected
        completedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completedSelected();
            }

            private void completedSelected() {

                int index = todoTable.getSelectedRow();
                System.out.println(index);

                //get the data from the selected row
                Vector data = todoModel.getDataVector().elementAt(index);
                System.out.println(data);//[asdf, fffff, 3, WORK] tasks, desc, priority, category

                String taskName = String.valueOf(data.get(0));
                System.out.println(taskName);

                //TODO call controller to update the status of it into completed for status.
                //DO I HAVE TO CHANGE THE TASKS object value too? update into completed?
                controller.updateByTaskName(taskName);
                updateTable();
                List<Tasks> allData = controller.loadAllTasksFromStore();
                setListData(allData);

                showMessageDialog("Successfully Updated");

            }
        });
        //if delete is being clicked
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelected();
            }

            private void deleteSelected() {
                int index = todoTable.getSelectedRow();
                System.out.println(index);

                //get the data from the selected row
                Vector data = todoModel.getDataVector().elementAt(index);
                System.out.println(data);//[asdf, fffff, 3, WORK] tasks, desc, priority, category

                String taskName = String.valueOf(data.get(0));
                System.out.println(taskName);

                //TODO call controller to delete from DB
                controller.deleteTasks(taskName);
                updateTable();
                List<Tasks> allData = controller.loadAllTasksFromStore();
                setListData(allData);

                showMessageDialog("Successfully Deleted");

                //TODO update table and the JList down there


            }
        });


        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        completedTaksButton.addActionListener(new ActionListener() {
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
                    Tasks value = searchList.getSelectedValue();

                    Tasks getTasks = controller.searchByID(value.getTasksID());

                    getTasks.setDateCompleted(new Date());

                    getTasks.setStatus(Tasks.TasksStatus.COMPLETED);

                    controller.updateTasks(getTasks);

                    showMessageDialog("Tasks Updated");

                    updateTable();

                    searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                    List<Tasks> allData = controller.loadAllTasksFromStore();
                    setListData(allData);

                }
            }

        });

        searchByPriority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //                    int priority =priorityComboBox.getItemAt(priorityComboBox.getSelectedIndex());

                if(searchByPriorityComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a priority level");
                }else{
                    int p = searchByPriorityComboBox.getItemAt(searchByPriorityComboBox.getSelectedIndex());
                    List<Tasks> searchTasks = new ArrayList<>();
                    
                    searchTasks = controller.searchByPriority(p);
                    
                    searchPResults(searchTasks);
                }

                searchByPriorityComboBox.setSelectedIndex(-1);
            }

            private void searchPResults(List<Tasks> search) {

                if(search==null||search.isEmpty()){
                    searchListModel.clear();
                    searchListDescriptionLabel.setText(ToDoListManager.NO_TASKS_FOUND);
                }else{

                    searchListDescriptionLabel.setText(ToDoListManager.MATCHING_TASKS);
                    setListData(search);
                }
            }
        });

        searchByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                if(searchByCategoryComboBox.getSelectedIndex()<0){
                    showMessageDialog("Please select a category to  search for");
                }else{
                    String categorySelected =searchByCategoryComboBox.getItemAt(searchByCategoryComboBox.getSelectedIndex());

                    List<Tasks> searchTasks = new ArrayList<>();

                    searchTasks = controller.searchByCategory(categorySelected);

                    searchCategoryResults(searchTasks);
                }
                searchByCategoryComboBox.setSelectedIndex(-1);

            }

            private void searchCategoryResults(List<Tasks> search) {

                if(search==null||search.isEmpty()){
                    searchListModel.clear();
                    searchListDescriptionLabel.setText(ToDoListManager.NO_TASKS_FOUND);
                }else{

                    searchListDescriptionLabel.setText(ToDoListManager.MATCHING_TASKS);
                    setListData(search);
                }

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //int selectedIndex;
                Tasks t;

                if(searchList.getSelectedValue() == null){
                    showMessageDialog("Please select one task from the table below to edit");
                }else{
                    t = searchList.getSelectedValue();
                    proceedEdit(t);
                }

            }

            private void proceedEdit(Tasks t) {
                String newDescription = showInputDialog("Enter text for your new description");
                t.setDescription(newDescription);
                controller.updateTasks(t);
                updateTable();

                searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
                List<Tasks> allData = controller.loadAllTasksFromStore();
                setListData(allData);

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

        priorityLModel = new DefaultComboBoxModel<>();
        categoryLModel = new DefaultComboBoxModel<>();

        for (int value : index) {

            priorityLModel.addElement(value);
        }

        for(int l=0; l<categoryList.length;l++){
            categoryLModel.addElement(categoryList[l]);
        }



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

        searchByPriorityComboBox.setModel(priorityLModel);
        //TODO This will need to makesure the user clicked on the priority comboBox
        searchByPriorityComboBox.setSelectedIndex(-1);

        searchByCategoryComboBox.setModel(categoryLModel);
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
