package xyz.me4cxy.easyservice.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jayin
 * @create 2020/10/28
 */
@Service
public class TestService {
    public List list() {
        return new ArrayList(){{
            this.add(1);
            this.add(2);
        }};
    }

    public Map<String, Object> get(Integer id) {
        return new HashMap(){{
            this.put("id", id);
            this.put("name", "jayin");
        }};
    }
}