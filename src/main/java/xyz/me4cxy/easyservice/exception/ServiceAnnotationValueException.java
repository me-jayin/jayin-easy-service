package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/29
 */
public class ServiceAnnotationValueException extends RuntimeException {
    public ServiceAnnotationValueException() {
        super();
    }

    public ServiceAnnotationValueException(String message) {
        super(message);
    }

    public ServiceAnnotationValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceAnnotationValueException(Throwable cause) {
        super(cause);
    }

    protected ServiceAnnotationValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}