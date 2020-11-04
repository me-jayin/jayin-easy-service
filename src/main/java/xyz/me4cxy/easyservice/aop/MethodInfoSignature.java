package xyz.me4cxy.easyservice.aop;

import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import xyz.me4cxy.easyservice.processer.ServiceProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Jayin
 * @create 2020/10/28
 */
public class MethodInfoSignature {
    private MethodInvocationProceedingJoinPoint source;
    private Method method;
    private Annotation easyApiAnnotation;
    private String[] paramNames;
    private ServiceProcessor serviceProcessor;

    public MethodInfoSignature(MethodInvocationProceedingJoinPoint source, Method method, Annotation easyApiAnnotation, String[] paramNames) {
        this.source = source;
        this.method = method;
        this.easyApiAnnotation = easyApiAnnotation;
        this.paramNames = paramNames;
    }

    public MethodInfoSignature(MethodInvocationProceedingJoinPoint source) {
        this.source = source;
    }

    public MethodInvocationProceedingJoinPoint getSource() {
        return source;
    }

    public void setSource(MethodInvocationProceedingJoinPoint source) {
        this.source = source;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Annotation getEasyApiAnnotation() {
        return easyApiAnnotation;
    }

    public void setEasyApiAnnotation(Annotation easyApiAnnotation) {
        this.easyApiAnnotation = easyApiAnnotation;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public ServiceProcessor getServiceProcessor() {
        return serviceProcessor;
    }

    public void setServiceProcessor(ServiceProcessor serviceProcessor) {
        this.serviceProcessor = serviceProcessor;
    }
}