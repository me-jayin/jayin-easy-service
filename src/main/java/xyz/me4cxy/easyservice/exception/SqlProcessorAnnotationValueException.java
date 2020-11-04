package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class SqlProcessorAnnotationValueException extends SqlProcessorException {
    public SqlProcessorAnnotationValueException() {
        super();
    }

    public SqlProcessorAnnotationValueException(String message) {
        super(message);
    }

    public SqlProcessorAnnotationValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlProcessorAnnotationValueException(Throwable cause) {
        super(cause);
    }

    protected SqlProcessorAnnotationValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}