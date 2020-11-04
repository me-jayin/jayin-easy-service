package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class SqlProcessorException extends RuntimeException {
    public SqlProcessorException() {
        super();
    }

    public SqlProcessorException(String message) {
        super(message);
    }

    public SqlProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlProcessorException(Throwable cause) {
        super(cause);
    }

    protected SqlProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}