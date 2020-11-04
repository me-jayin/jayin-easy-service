package xyz.me4cxy.easyservice.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的结果处理器
 * @author Jayin
 * @create 2020/10/27
 */
public class DefaultResultHandler implements ResultHandler<Object> {
    @Override
    public Object handle(Object result) {
        Map<String, Object> rs = new HashMap<>(4);
        if (result != null) {
            rs.put("data", result);
            rs.put("success", 1);
            rs.put("msg", "成功");
        } else {
            rs.put("success", 0);
            rs.put("msg", "失败");
        }
        return rs;
    }
}