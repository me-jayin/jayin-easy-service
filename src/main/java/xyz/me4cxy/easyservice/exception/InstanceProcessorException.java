package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/28
 */
public class InstanceProcessorException extends RuntimeException {
    public InstanceProcessorException() {
        super();
    }

    public InstanceProcessorException(String message) {
        super(message);
    }

    public InstanceProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceProcessorException(Throwable cause) {
        super(cause);
    }

    protected InstanceProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}