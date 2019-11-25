import javax.swing.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ToDoListManager extends JFrame{
    private JPanel mainPanel;
    private JLabel titleLabel;

    private JPanel addTasksPanel;
    private JTextField tasksNameText;
    private JTextField descriptionText;
    private JComboBox priorityComboBox;
    private JComboBox categoryComboBox;
    private JButton addButton;
    private JLabel tasksNameLabel;
    private JLabel descriptionLabel;
    private JLabel priorityLabel;
    private JLabel categoryLabel;
    private JList incompleList;
    private JPanel toDoListPanel;
    private JButton completedTaksButton;
    private JButton showAllTasks;
    private JList CompletedList;
    private JLabel toDoLabel;
    private JLabel completedList;
    private JComboBox searchByPriorityComboBox;
    private JButton searchByPriority;
    private JButton searchByCategoryButton;
    private JPanel searchPanel;
    private JComboBox searchByCategoryComboBox;
    private JList searchList;
    private JLabel searchListDescription;
    private JButton quitButton;
    private JPanel controlPanel;
    private JButton editButton;
}
