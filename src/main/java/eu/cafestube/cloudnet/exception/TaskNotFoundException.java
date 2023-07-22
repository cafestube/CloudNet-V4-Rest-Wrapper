package eu.cafestube.cloudnet.exception;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String task) {
        super("Task " + task + " not found");
    }
}
