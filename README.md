Task Manager Program
====================

This program is mainly to deal with your `to-do list`. It is a very intuitive applications and easy to navigate around. 
This application contains two GUI window: `ToDoListManager` and `searchGUI`.

Database is stored under the `ListStore` and is being controlled through `ListController`.

`Task` class is being created to handled the Task object.

There are several features as listed below: 
- `Add` new task
- `View` incomplete and completed task
- Marked as `completed` for `incomplete Task`
- `Show all` the incomplete tasks
- `Edit description` for incomplete task
- `Search` task either by priority or category
- `Delete` task 
- `Sort view `on the todo list either sort by name or by priority or category

Features on the `ToDo Table` and `Completed Table` :
- Able to right click on the `ToDo Table` to either **delete** or marked as **completed** as well as show the **description**
- On the `Completed Table` you are able to right click and **delete** the selected task 

`searhGUI`:
- This will be show up when you added a new task, do `SearchByPriority` or `SearchByCategory`, `ShowAllIncompleteTasks` and `EditDescriptionForSelectedTask`

*Bugs/Problems:*
- `searchGUI` are not able to resize to specify dimension and will have to adjust the size when it pops up


