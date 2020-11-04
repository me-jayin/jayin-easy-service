package xyz.me4cxy.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Jayin
 * @create 2020/10/29
 */
public class FieldUtils {
    public static final String SEPARATOR = ".";

    /**
     * 获取Map中指定属性的值
     * @param mapping
     * @param nestedFields
     * @param ifNullThrowException
     * @return
     */
    public static Object getFieldValueByMap(Map<String, Object> mapping, String nestedFields, boolean ifNullThrowException) {
        if (StringUtils.isEmpty(nestedFields)) {
            getFieldValue(null, nestedFields, ifNullThrowException); // 委托给其他方法抛出异常
        }

        int spIndex = nestedFields.indexOf(SEPARATOR);
        if (spIndex < 0) return mapping.get(nestedFields);
        Object result = mapping.get(nestedFields.substring(0, spIndex));
        return getFieldValue(result, nestedFields.substring(spIndex + 1), ifNullThrowException);
    }

    /**
     * 获取指定对象的属性值
     * @param target
     * @param nestedFields 需要的属性表达式，可以 . 进行深层获取
     * @param ifNullThrowException
     * @return
     */
    public static Object getFieldValue(Object target, String nestedFields, boolean ifNullThrowException) {
        return getFieldValue(target, target == null ? null : target.getClass(), nestedFields, ifNullThrowException, new ArrayList<>());
    }

    private static Object getFieldValue(Object target, Class targetClazz, String nestedFields, boolean ifNullThrowException, List<String> trace) {
        if (target == null) {
            if (ifNullThrowException) {
                if (trace == null || trace.isEmpty())
                    throw new NullPointerException("目标对象不能为空");
                else
                    throw new NullPointerException("目标对象中【" + CollectionUtils.join(trace, SEPARATOR) + "】的属性值为空");
            }
            return null;
        }
        // 如果是获取当前的对象
        if ("".equals(nestedFields)) return target;
        // 获取分隔符 . 的位置
        int indexSp = nestedFields.indexOf(SEPARATOR);
        String fieldName = nestedFields;
        if (indexSp >= 0)
            fieldName = nestedFields.substring(0, indexSp);

        try {
            // 记录属性获取轨迹
            trace.add(fieldName);
            Field field = targetClazz.getDeclaredField(fieldName);
            if (!field.isAccessible()) field.setAccessible(true);

            target = field.get(target);
            // 如果存在 . 说明有下级
            if (indexSp > -1) {
                nestedFields = nestedFields.substring(indexSp + 1);
            } else { // 如果没有返回最终结果
                return target;
            }

            return getFieldValue(target, target == null ? null : target.getClass(), nestedFields, ifNullThrowException, trace);
        } catch (NoSuchFieldException e) {
            throw new NoSuchElementException("目标对象中不存在指定属性【" + CollectionUtils.join(trace, SEPARATOR) + "】");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("目标对象中属性【" + CollectionUtils.join(trace, SEPARATOR) + "】无访问权限");
        }
    }
}