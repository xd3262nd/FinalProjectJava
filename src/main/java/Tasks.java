import java.util.Date;

public class Tasks {

    enum TasksStatus{
        INCOMPLETE, COMPLETED
    }

    enum TasksCategory{
        PERSONAL, WORK
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
        this.status = TasksStatus.INCOMPLETE;
        this.category = cate;
    }

    public Tasks(int id, String name, String desc, Date dateCreated, int p, TasksCategory cate, Date dateCompleted, TasksStatus status ){
        this.tasksID=id;
        this.name=name;
        this.description=desc;
        this.dateCreated=dateCreated;
        this.dateCompleted=dateCompleted;
        this.priority=p;
        this.category = cate;
        this.status = status;
    }
//TODO if only need this
//    //constructor to be return for the table
//    public Tasks(String name, String desc, int p, TasksCategory cate){
//        this.name = name;
//        this.description = desc;
//        this.priority = p;
//        this.category = cate;
//    }



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

    public String toString(){

        String statement = String.format("ID: %d, Tasks: %s, Description: %s, Priority: %d, Category: %s, Created on: %s. ",
                this.tasksID, this,name, this.description, this.priority, this.category, this.dateCreated);

        if(dateCompleted !=null && dateCompleted.getTime() !=0){
            statement += "Completed on: " + dateCompleted;
        }

        statement += " Status: " + status.name();

        return statement;



    }



}
