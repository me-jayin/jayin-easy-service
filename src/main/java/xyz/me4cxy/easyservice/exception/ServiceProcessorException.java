package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/29
 */
public class ServiceProcessorException extends RuntimeException {

    public ServiceProcessorException() {
        super();
    }

    public ServiceProcessorException(String message) {
        super(message);
    }

    public ServiceProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceProcessorException(Throwable cause) {
        super(cause);
    }

    protected ServiceProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}