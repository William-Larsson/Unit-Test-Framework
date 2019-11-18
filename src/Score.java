/**
 * A class for keeping track of the amount of succeeded, failed and
 * fails due to exceptions that have occurred in a thread-safe manner.
 */
public class Score {
    private int success;
    private int fail;
    private int exception;

    public Score(){
        success = 0;
        fail = 0;
        exception = 0;
    }

    public synchronized int getSuccess() {
        return success;
    }

    public synchronized int getFail(){
        return fail;
    }

    public synchronized int getException() {
        return exception;
    }

    public synchronized void setException(int exception) {
        this.exception = exception;
    }

    public synchronized void setFail(int fail) {
        this.fail = fail;
    }

    public synchronized void setSuccess(int success) {
        this.success = success;
    }
}
