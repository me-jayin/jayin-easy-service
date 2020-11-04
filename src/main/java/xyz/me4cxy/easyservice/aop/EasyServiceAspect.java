package xyz.me4cxy.easyservice.aop;

import com.sun.xml.internal.txw2.IllegalAnnotationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import xyz.me4cxy.easyservice.annotations.EasyServiceProcessor;
import xyz.me4cxy.easyservice.annotations.EasyServiceResultHandler;
import xyz.me4cxy.easyservice.annotations.EasyService;
import xyz.me4cxy.easyservice.annotations.manager.DefaultEasyServiceObtainerManager;
import xyz.me4cxy.easyservice.annotations.manager.EasyServiceObtainerManager;
import xyz.me4cxy.easyservice.annotations.obtainer.ServiceResultAnnotationObtainer;
import xyz.me4cxy.easyservice.annotations.obtainer.ExecuteSqlAnnotationObtainer;
import xyz.me4cxy.easyservice.exception.ServiceProcessorNotSupportException;
import xyz.me4cxy.easyservice.processer.ServiceProcessor;
import xyz.me4cxy.easyservice.result.DefaultResultHandler;
import xyz.me4cxy.easyservice.result.ResultHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jayin
 * @create 2020/10/27
 */
@Aspect
public class EasyServiceAspect implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(EasyServiceAspect.class);

    private ApplicationContext applicationContext;
    /**
     * 本地变量表侦测器
     */
    private LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    /**
     * EasyApi注解获取管理器
     * 内置多种类型的注解获取器，通过逐个遍历得到实际的注解对应的处理器进行处理请求
     */
    private EasyServiceObtainerManager easyServiceObtainerManager;
    /**
     * 方法信息缓存
     */
    private ConcurrentHashMap<Method, MethodInfoSignature> methodInfoCache = new ConcurrentHashMap<>();
    /**
     * 结果处理器缓存
     */
    private ConcurrentHashMap<Class, ResultHandler> resultHandlerCache = new ConcurrentHashMap<>();

    /**
     * 全局默认结果处理器.
     */
    private ResultHandler defaultResultHandler;

    public EasyServiceAspect() {
        this(new DefaultEasyServiceObtainerManager(Arrays.asList(
                new ServiceResultAnnotationObtainer(),
                new ExecuteSqlAnnotationObtainer()
        )));
    }

    public EasyServiceAspect(EasyServiceObtainerManager easyServiceObtainerManager) {
        this(easyServiceObtainerManager, new DefaultResultHandler());
    }

    public EasyServiceAspect(EasyServiceObtainerManager easyServiceObtainerManager, ResultHandler defaultResultHandler) {
        this.easyServiceObtainerManager = easyServiceObtainerManager;
        this.defaultResultHandler = defaultResultHandler;
    }

    /**
     * 代理被 #{@link EasyService} 注解修饰的控制器
     */
    @Pointcut("execution(* (@xyz.me4cxy.easyservice.annotations.EasyService *).*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodInvocationProceedingJoinPoint methodJp = (MethodInvocationProceedingJoinPoint) joinPoint;

        // 获取方法信息签名
        MethodInfoSignature mis = getMethodInfoSignature(methodJp);
        if (mis == null) { // 不存在签名则说明不需要进行预处理
            return joinPoint.proceed(joinPoint.getArgs());
        }

        // 解析方法参数映射
        Map<String, Object> argsMapping = resolveArgsMapping(mis, joinPoint.getArgs());
        // 进行按需装配业务处理器
        this.initServiceProcessor(mis);
        ServiceProcessor processor = mis.getServiceProcessor();
        if (!processor.isSupport(mis)) {
            throw new ServiceProcessorNotSupportException("业务处理器【" + mis.getServiceProcessor().getClass()
                    + "】不支持当前该类型的easy service处理方式【" + mis.getEasyApiAnnotation().getClass() + "】");
        }
        // 通过业务处理器获取结果
        Object result = mis.getServiceProcessor().doService(mis, argsMapping);
        // 获取结果处理器进行处理结果
        ResultHandler handler = this.getResultHandler(mis);
        return handler.handle(result);
    }

    /**
     *
     * @param mis
     * @return
     */
    private ResultHandler getResultHandler(MethodInfoSignature mis) {
        EasyServiceResultHandler handlerAnno = AnnotationUtils.getAnnotation(mis.getMethod(), EasyServiceResultHandler.class);
        if (handlerAnno == null) return this.defaultResultHandler;

        // 结果处理器注册为bean对象
        if (!StringUtils.isEmpty(handlerAnno.handlerBean())) {
            return this.applicationContext.getBean(handlerAnno.handlerBean(), ResultHandler.class);
        }

        Class<? extends ResultHandler> handlerClass = handlerAnno.handlerClass();
        ResultHandler handler = resultHandlerCache.get(handlerClass), temp;
        if (handler != null) return handler;
        handler = applicationContext.getAutowireCapableBeanFactory().createBean(handlerClass);
        if ((temp = resultHandlerCache.putIfAbsent(handlerClass, handler)) != null)
            return temp;
        return handler;
    }

    /**
     * 获取方法信息签名
     * @param methodJp
     * @return
     */
    private MethodInfoSignature getMethodInfoSignature(MethodInvocationProceedingJoinPoint methodJp) {
        MethodSignature signature = (MethodSignature) methodJp.getSignature();
        Method method = signature.getMethod();

        Annotation anno = null;
        // 判断当前方法是否需要需要使用EasyApi
        if ((anno = this.easyServiceObtainerManager.findProcessAnno(method)) == null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("方法【{}】不需要进行easy service处理", method);
            }
            return null;
        }

        // 尝试获取缓存中的 MethodInfoSignature
        MethodInfoSignature mis;
        if ((mis = methodInfoCache.get(method)) != null) return mis;

        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        mis = new MethodInfoSignature(methodJp, method, anno, paramNames);
        // 如果已经put时存在缓存则直接返回
        if (methodInfoCache.putIfAbsent(method, mis) != null)
            return methodInfoCache.get(method);
        return mis;
    }

    /**
     * 参数解析，转成 参数名 -> 参数值 的map
     * @param mis
     * @param args
     * @return
     */
    private Map<String, Object> resolveArgsMapping(MethodInfoSignature mis, Object[] args) {
        Map<String, Object> argsMapping = new HashMap<>(args.length);
        // 解析方法入参名
        String[] names = mis.getParamNames();
        if (names.length != args.length) {
            throw new IllegalArgumentException("方法【" + mis.getMethod() + "】声明的入参个数与实时切面的入参个数不一致");
        } else if (names.length == 0) {
            return Collections.emptyMap();
        }
        // 遍历并设置入参对应的值
        for (int i = 0; i < names.length; i++) {
            argsMapping.put(names[i], args[i]);
        }
        return argsMapping;
    }

    /**
     * 按需进行初始化业务处理器，并设置到 MethodInfoSignature 中
     * @param mis
     */
    private void initServiceProcessor(MethodInfoSignature mis) {
        // 使用校验 - 锁 - 校验 的方式来确保单例
        if (mis.getServiceProcessor() == null) {
            synchronized (mis) {
                if (mis.getServiceProcessor() == null) {
                    // 获取 easy service 业务注解
                    Annotation easyServiceAnno = mis.getEasyApiAnnotation();
                    // 获取该注解中定制的 业务处理器
                    EasyServiceProcessor processorDefine = AnnotationUtils.getAnnotation(easyServiceAnno, EasyServiceProcessor.class);
                    if (processorDefine == null) {
                        throw new IllegalAnnotationException("easy service定制的处理类型【" + easyServiceAnno.getClass() + "】中未指定处理器（@EasyServiceProcessor）");
                    }
                    // 初始化业务注解器
                    ServiceProcessor processor = this.applicationContext.getAutowireCapableBeanFactory().createBean(processorDefine.value());
                    mis.setServiceProcessor(processor);
                }
            }
        }
    }

    public LocalVariableTableParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    public void setParameterNameDiscoverer(LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public EasyServiceObtainerManager getEasyServiceObtainerManager() {
        return easyServiceObtainerManager;
    }

    public void setEasyServiceObtainerManager(EasyServiceObtainerManager easyServiceObtainerManager) {
        this.easyServiceObtainerManager = easyServiceObtainerManager;
    }
}