package cn.qblank.logdemo.controller;

import cn.qblank.logdemo.annotion.LoggerProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/10/18
 */
@Controller
@RequestMapping("/test/")
public class UserController {

    @GetMapping("users")
    @LoggerProfile(description = "用户列表")
    @ResponseBody
    public List<String> findUsers(){
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("里斯");
        list.add("王五");
        list.add("王麻子");
        list.add("赵六");
        return list;
    }

    @GetMapping("user")
    @LoggerProfile(description = "查询单个列表")
    @ResponseBody
    public String findUserById(int id){
        String name = "evan_qb";
        return name;
    }

    @GetMapping("error")
    @LoggerProfile(description = "错误测试")
    @ResponseBody
    public String error(){
        int i = 1/0;
        return "错误";
    }
}
