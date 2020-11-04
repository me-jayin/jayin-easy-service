package xyz.me4cxy.easyservice.result;

/**
 * EasyApi结果处理器
 * @author Jayin
 * @create 2020/10/27
 */
public interface ResultHandler<T> {
    /**
     * 处理结果
     * @param result
     * @return
     */
    Object handle(T result);
}