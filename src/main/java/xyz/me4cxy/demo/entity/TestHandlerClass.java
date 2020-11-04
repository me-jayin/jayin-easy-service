package xyz.me4cxy.demo.entity;

import xyz.me4cxy.easyservice.result.ResultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jayin
 * @create 2020/11/4
 */
public class TestHandlerClass implements ResultHandler {

    @Override
    public Object handle(Object result) {
        Map<String, Object> r = new HashMap<>();
        if (result != null) {
            r.put("success", result);
        }
        return r;
    }
}