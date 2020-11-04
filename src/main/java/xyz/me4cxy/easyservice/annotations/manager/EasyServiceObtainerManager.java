package xyz.me4cxy.easyservice.annotations.manager;

import xyz.me4cxy.easyservice.annotations.obtainer.EasyServiceAnnotationObtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface EasyServiceObtainerManager {

    void registerObtainer(EasyServiceAnnotationObtainer obtainer);

    Annotation findProcessAnno(AnnotatedElement ae);

}
