package com.irappelt.mymusic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.User;
import com.irappelt.mymusic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
@ExceptionCapture
public class UserController {

    @Autowired
    protected WebResponse webResponse;

    @Resource
    protected UserService userServiceImpl;


    /**
     * 登录功能
     */
    @RequestMapping(value = "/loginPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse loginCon(HttpServletRequest request, HttpSession session) {

        // 取参数的方法，对应登录表单中的用户名name="user_name"
        String user_name = request.getParameter("user_name");
        String user_password = request.getParameter("user_password");

        User user = userServiceImpl.getUser(user_name, user_password);

        if (user == null) {
            return webResponse.getWebResponse(201, "用户或密码错误！", null);
        }
        session.setAttribute("tname", user.getUserName());
        session.setAttribute("userId", user.getUserId());

        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("user_name", user_name);
        paramMap.put("user_password", user_password);

        return webResponse.getWebResponseUserId(200, "登录成功", paramMap, user.getUserId());

    }

    /**
     * 更改密码
     */
    @RequestMapping(value = "/resetUserPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse resetUserPassword(HttpServletRequest request) {

        User user = null;
        String user_name = request.getParameter("user_name");
        String newUser_password = request.getParameter("newUser_password");
        user = userServiceImpl.resetPassword(user_name, newUser_password);

        return webResponse.getWebResponse(200, "修改成功", user);
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/addOrEditUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addOrEditTest(String user_id, @RequestParam(required = false) String user_name, @RequestParam(required = false) String user_password) {
        if (userServiceImpl.isRepeatUser(user_name)) {
            return webResponse.getWebResponse(201, "用户名已存在，请重新注册！", null);
        }
        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("user_id", user_id);
        paramMap.put("user_name", user_name);
        paramMap.put("user_password", user_password);
        if (user_id == null || user_id.length() == 0) {
            User user = userServiceImpl.addUser(user_name, user_password);
            if (user == null) {
                return webResponse.getWebResponse(201, "注册失败：用户名和密码必填", paramMap);
            }
            return webResponse.getWebResponse(200, "注册成功", paramMap);
        } else {
            User user = userServiceImpl.updateUser(user_id, user_name, user_password);
            if (user == null) {
                return webResponse.getWebResponse(201, "更新失败：不存在该用户", paramMap);
            }
            return webResponse.getWebResponse(200, "更新成功", paramMap);
        }
    }
}
