package xyz.me4cxy.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Jayin
 * @create 2020/10/29
 */
public class CollectionUtils {

    /**
     * 将集合按指定分隔符拼成一个字符串
     * @param co
     * @param sp
     * @return
     */
    public static String join(Collection co, String sp) {
        StringBuilder sb = new StringBuilder();
        Iterator it = co.iterator();
        boolean isStart = true;
        while (it.hasNext()) {
            if (!isStart) sb.append(sp);
            sb.append(it.next());
            isStart = false;
        }
        return sb.toString();
    }

}