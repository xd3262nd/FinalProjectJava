import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class searchGUI extends JFrame {

    private JList <Task>  searchList;
    private JLabel searchListDescriptionLabel;
    private JPanel searchPanel;
    private JButton backToMainButton;

    protected DefaultListModel<Task> searchListModel;
    final ToDoListManager parentComponent;


    searchGUI(ToDoListManager parentComponent){
        this.parentComponent = parentComponent;

        setTitle("Search List");

        setContentPane(searchPanel);
        setPreferredSize(new Dimension(700,700));
        pack();

        setVisible(true);
        parentComponent.setEnabled(false);
        setLocationRelativeTo(null);


        //initialize the List Model for search JList
        searchListModel = new DefaultListModel<>();
        searchList.setModel(searchListModel);
        //the program will show all incomplete tasks when it starts
        searchListDescriptionLabel.setText(ToDoListManager.ALL_TASKS);
        searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addListener();
    }

    private void addListener() {
        //button to return back to main screen
        backToMainButton.addActionListener(e -> {

            parentComponent.setEnabled(true);
            searchGUI.this.dispose();
        });

        //makesure when user clicked on the close window button
        //main screen will be enabled too
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentComponent.setEnabled(true);
                searchGUI.this.dispose();
                super.windowClosing(e);
            }
        });
    }
//return the search list on the search GUI
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
