package xyz.me4cxy.easyservice.processer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import xyz.me4cxy.easyservice.annotations.ServiceResult;
import xyz.me4cxy.easyservice.aop.MethodInfoSignature;
import xyz.me4cxy.easyservice.exception.ServiceAnnotationValueException;
import xyz.me4cxy.easyservice.exception.ServiceProcessorBeanNoSuchMethodException;
import xyz.me4cxy.easyservice.exception.ServiceProcessorException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jayin
 * @create 2020/10/27
 */
public class ServiceBeanServiceProcessor implements ServiceProcessor, ApplicationContextAware {
    public static final String[] EMPTY_ARGS = new String[0];

    /**
     * bean的名称和方法分割符
     */
    public static final String SEPARATOR_CHAR = ".";
    /**
     * spring 应用上下文
     */
    private ApplicationContext applicationContext;
    /**
     * 表达式对象缓存
     */
    private ConcurrentHashMap<Method, InvokeExpression> expressionCache = new ConcurrentHashMap<>();

    @Override
    public Object doService(MethodInfoSignature mis, Map<String, Object> argsMapping) {
        InvokeExpression expression = this.getExpression(mis);
//        Object[] values = expression.getArgsByExpression(argsMapping);
        Object[] values = argsMapping.values().toArray();
        try {
            return expression.method.invoke(expression.bean, values);
        } catch (Exception e) {
            throw new ServiceProcessorException("方法【" + expression.getMethod() + "】调用失败");
        }
    }

    /**
     * 获取表达式对象.
     * 首先从缓存中获取，如果不存在则进行实例创建
     * @param mis
     * @return
     */
    private InvokeExpression getExpression(MethodInfoSignature mis) {
        InvokeExpression expression = expressionCache.get(mis.getMethod()), temp;
        if (expression == null) {
            expression = InvokeExpression.getInstance((ServiceResult) mis.getEasyApiAnnotation(), mis.getMethod().getParameterTypes(), this.applicationContext);
            if ((temp = expressionCache.putIfAbsent(mis.getMethod(), expression)) != null) {
                return temp;
            }
        }
        return expression;
    }

    @Override
    public boolean isSupport(MethodInfoSignature mis) {
        return ServiceResult.class.isInstance(mis.getEasyApiAnnotation());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static class InvokeExpression {
        private Object bean;
        private Method method;

        private InvokeExpression(Object beanRefer, String method, Class[] paramTypes, ApplicationContext applicationContext) { // , String[] args
            this.bean = this.getBean(beanRefer, applicationContext);

            Class clazz = this.bean.getClass();
            try {
                this.method = clazz.getDeclaredMethod(method, paramTypes);
                if (!this.method.isAccessible()) this.method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new ServiceProcessorBeanNoSuchMethodException("bean【" + beanRefer + "】中找不到方法【" + method + "】", e);
            }
        }

        public Object getBean() {
            return bean;
        }

        public void setBean(Object bean) {
            this.bean = bean;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public static InvokeExpression getInstance(ServiceResult target, Class[] paramTypes, ApplicationContext applicationContext) {
            // 使用 bean class 进行获取bean
            if (StringUtils.isEmpty(target.beanName())) {
                if (target.beanClass().equals(Object.class)) {
                    throw new ServiceAnnotationValueException("@ServiceResult(...)中 beanName 及 beanClass & method 属性未指定业务bean方法表达式");
                } else if (StringUtils.isEmpty(target.method())) {
                    throw new ServiceAnnotationValueException("@ServiceResult(...)中 method 方法表达式未空");
                }

                checkMethodExpression(target.method());
                return new InvokeExpression(target.beanClass(), target.method(), paramTypes, applicationContext);
            }

//            // 使用 bean方法表达式 执行业务
//            String beanRefer = target.beanName();
//            int startSeparator = beanRefer.indexOf(SEPARATOR_CHAR), openBrackets = beanRefer.indexOf("(");
//            if (startSeparator >= openBrackets) {
//                throw new ServiceAnnotationValueException("@ServiceResult(...)值无效，请检查方法风格符“.”是否在“(”之前");
//            }
//            // 切割bean的名称
//            String beanName = beanRefer.substring(0, startSeparator);
//            if (StringUtils.isEmpty(beanName)) {
//                throw new ServiceAnnotationValueException("@ServiceResult(...)值并未指定bean名称");
//            }
//            // 获取方法表达式
//            String methodExpression = beanRefer.substring(startSeparator + 1);
//            checkMethodExpression(methodExpression);

            // 创建方法调用表达式对象
            return new InvokeExpression(target.beanName(), target.method(), paramTypes, applicationContext); // , getMethodExpressionArgs(methodExpression)
        }

        /**
         * 校验执行方法表达式
         * @param methodExpression
         */
        private static void checkMethodExpression(String methodExpression) { }

//        /**
//         * 获取表达式中实际的参数值
//         * @param argsMapping
//         * @return
//         */
//        public Object[] getArgsByExpression(Map<String, Object> argsMapping) {
//            String[] expressions = this.args;
//            Object[] args = new Object[expressions.length];
//
//            for (int i = 0; i < expressions.length; i++) {
//                String express = expressions[i];
//                int indexSp = express.indexOf(".");
//                if (indexSp < 0) {
//                    args[i] = argsMapping.get(express);
//                    continue;
//                }
//
//                Object value = express.substring(0, indexSp);
//                // 获取指定属性的值，如果值为空则返回null
//                args[i] = FieldUtils.getFieldValue(value, express.substring(indexSp + 1), false);
//            }
//
//            return args;
//        }

        /**
         * 获取每个参数的表达式
         * @param methodExpression
         * @return
         */
        private static String[] getMethodExpressionArgs(String methodExpression) {
            int openIndex = methodExpression.indexOf("("), closeIndex = methodExpression.indexOf(")");
            if (openIndex < 0 || closeIndex < 0) {
                throw new ServiceAnnotationValueException("方法表达式中参数不存在");
            }

            // 获取括号内的参数表达式
            String argExpression = methodExpression.substring(openIndex + 1, closeIndex);
            if ("...".equals(argExpression)) {
                return null;
            }

            // 切割参数
            String[] args = StringUtils.split(argExpression, ",");
            return args == null ? EMPTY_ARGS : args;
        }

        /**
         * 获取指定bean对象
         * @param beanRefer
         * @param applicationContext
         * @return
         */
        private Object getBean(Object beanRefer, ApplicationContext applicationContext) {
            Object bean = null;
            if (applicationContext == null) {
                throw new ServiceAnnotationValueException("应用上下文为空");
            } else if (beanRefer == null) {
                throw new ServiceAnnotationValueException("无效的bean引用");
            } else if (beanRefer.getClass().equals(String.class)) {
                bean = applicationContext.getBean((String) beanRefer);
            } else if (beanRefer.getClass().equals(Class.class)) {
                bean = applicationContext.getBean((Class) beanRefer);
            }
            if (bean == null) {
                throw new ServiceAnnotationValueException("无效的业务bean【" + beanRefer + "】");
            }
            return bean;
        }
    }
}