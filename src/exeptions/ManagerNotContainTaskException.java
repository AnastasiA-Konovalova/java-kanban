package exeptions;

public class ManagerNotContainTaskException extends RuntimeException {
    public ManagerNotContainTaskException(String message) {
        super(message);
    }
}
