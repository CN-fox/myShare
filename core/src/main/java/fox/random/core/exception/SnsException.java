package fox.random.core.exception;

/**
 * Created by w_q on 14-10-14.
 */
public class SnsException extends Exception {
    public SnsException() {
    }

    public SnsException(String detailMessage) {
        super(detailMessage);
    }

    public SnsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SnsException(Throwable throwable) {
        super(throwable);
    }
}
