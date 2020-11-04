package xyz.me4cxy.easyservice.processer;

import xyz.me4cxy.easyservice.aop.MethodInfoSignature;

import java.util.Map;

/**
 * 业务处理器
 */
public interface ServiceProcessor {
    /**
     * 根据 easy service 所需要的处理方式执行业务处理
     * @return
     */
    Object doService(MethodInfoSignature mis, Map<String, Object> args);

    /**
     * 判断当前的业务处理器是否支持当前方法.
     * @param mis
     * @return
     */
    boolean isSupport(MethodInfoSignature mis);
}
