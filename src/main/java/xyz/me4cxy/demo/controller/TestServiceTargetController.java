package xyz.me4cxy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import xyz.me4cxy.easyservice.annotations.EasyService;
import xyz.me4cxy.easyservice.annotations.ExecuteSql;
import xyz.me4cxy.easyservice.annotations.ServiceResult;
import xyz.me4cxy.demo.entity.User;

/**
 * @author Jayin
 * @create 2020/10/24
 */
@EasyService
public class TestServiceTargetController {
    @GetMapping("/get")
    @ServiceResult(beanName = "testService", method = "get")
    public Object test(Integer id) { return null;  }


    @GetMapping("/list")
    @ServiceResult(beanName = "testService", method = "list")
    public Object list() {return null;}

    @GetMapping("/user")
    @ExecuteSql(sql = "select username AS name from user where username = ${user.name}", resultType = User.class)
//    @EasyServiceResultHandler(handlerClass = TestHandlerClass.class)
    public Object get(User user) { return null; }
}