package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/28
 */
public class ServiceProcessorNotSupportException extends ServiceProcessorException {
    public ServiceProcessorNotSupportException() {
        super();
    }

    public ServiceProcessorNotSupportException(String message) {
        super(message);
    }

    public ServiceProcessorNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceProcessorNotSupportException(Throwable cause) {
        super(cause);
    }

    protected ServiceProcessorNotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}