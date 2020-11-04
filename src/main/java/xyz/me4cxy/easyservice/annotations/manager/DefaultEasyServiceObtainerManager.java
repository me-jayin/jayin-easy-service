package xyz.me4cxy.easyservice.annotations.manager;

import xyz.me4cxy.easyservice.annotations.obtainer.EasyServiceAnnotationObtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import java.util.List;

/**
 * 默认的EasyApi接口过滤处理管理器
 * @author Jayin
 * @create 2020/10/24
 */
public class DefaultEasyServiceObtainerManager implements EasyServiceObtainerManager {
    private List<EasyServiceAnnotationObtainer> filters;

    public DefaultEasyServiceObtainerManager(List<EasyServiceAnnotationObtainer> filters) {
        this.filters = filters;
    }

    @Override
    public void registerObtainer(EasyServiceAnnotationObtainer obtainer) {
        if (obtainer == null) throw new NullPointerException("无效的EasyService注解获取器");
        this.filters.add(obtainer);
    }

    /**
     * 判断当前允许被注解的元素是否需要被代理
     * @param ae
     * @return
     */
    @Override
    public Annotation findProcessAnno(AnnotatedElement ae) {
        Annotation anno;
        Iterator<EasyServiceAnnotationObtainer> it = filters.iterator();
        while (it.hasNext()) {
            EasyServiceAnnotationObtainer f = it.next();
            if ((anno = f.findEasyApiAnnotation(ae)) != null)
                return anno;
        }
        return null;
    }
}