import java.util.Date;

public class Tasks {

    enum TasksStatus{
        Incomplete, Completed
    }

    enum TasksCategory{
        Personal, Work
    }

    private int tasksID;

    private int priority;
    private String name;
    private String description;
    private Date dateCreated;

    private Date dateCompleted;

    private TasksStatus status;

    private TasksCategory category;

    //constructor for adding new Tasks
    public Tasks(String name, String desc, Date dateCreated, int p, TasksCategory cate){
        this.name = name;
        this.description = desc;
        this.dateCreated=dateCreated;
        this.priority = p;
        this.status = TasksStatus.Incomplete;
        this.category = cate;
    }




    //getter and setter for each variables
    public int getTasksID() {
        return tasksID;
    }

    public void setTasksID(int tasksID) {
        this.tasksID = tasksID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public void setStatus(TasksStatus status) {
        this.status = status;
    }

    public TasksCategory getCategory() {
        return category;
    }

    public void setCategory(TasksCategory category) {
        this.category = category;
    }


    //create the toString method here - to be printed in the List
    //TODO might need to change my GUI design



}
