package xyz.me4cxy.easyservice.annotations.obtainer;

import xyz.me4cxy.easyservice.annotations.ServiceResult;

/**
 * 获取service业务bean对象目标
 * @author Jayin
 * @create 2020/10/24
 */
public class ServiceResultAnnotationObtainer implements EasyServiceAnnotationObtainer {

//    @Override
//    public Annotation findEasyApiAnnotation(AnnotatedElement ae) {
//        Annotation anno = AnnotationUtils.getAnnotation(ae, annotationType());
//        return anno;
//    }

    @Override
    public Class annotationType() {
        return ServiceResult.class;
    }
}