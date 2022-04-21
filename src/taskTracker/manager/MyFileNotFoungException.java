package taskTracker.manager;

public class MyFileNotFoungException extends RuntimeException{

    public MyFileNotFoungException(final String message) {
        super(message);
    }
}
