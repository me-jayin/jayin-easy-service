package xyz.me4cxy.easyservice.exception;

/**
 * @author Jayin
 * @create 2020/10/29
 */
public class ServiceProcessorBeanNoSuchMethodException extends ServiceProcessorException {
    public ServiceProcessorBeanNoSuchMethodException() {
        super();
    }

    public ServiceProcessorBeanNoSuchMethodException(String message) {
        super(message);
    }

    public ServiceProcessorBeanNoSuchMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceProcessorBeanNoSuchMethodException(Throwable cause) {
        super(cause);
    }

    protected ServiceProcessorBeanNoSuchMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}