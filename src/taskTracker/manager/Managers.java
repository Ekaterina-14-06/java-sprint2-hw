package taskTracker.manager;

public abstract class Managers implements TaskManager {

    @Override
    public void addTask() {};

    @Override
    public void deleteAllTasks() {};

    @Override
    public void removeTask() {};

    @Override
    public void showAllTasks() {};

    @Override
    public void showOneTask() {};

    @Override
    public void changeTask() {};

    @Override
    public void changeStatus() {};
}
