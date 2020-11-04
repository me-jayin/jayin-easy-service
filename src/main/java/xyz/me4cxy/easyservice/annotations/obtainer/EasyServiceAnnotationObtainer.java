package xyz.me4cxy.easyservice.annotations.obtainer;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface EasyServiceAnnotationObtainer {
    default Annotation findEasyApiAnnotation(AnnotatedElement ae) {
        Annotation anno = AnnotationUtils.getAnnotation(ae, annotationType());
        return anno;
    }

    Class annotationType();
}
