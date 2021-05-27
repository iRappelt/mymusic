package com.irappelt.mymusic.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("userName", user_name);
        paramMap.put("userAvatar", user.getAvatarLink());

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
    public WebResponse addOrEditTest(String userId, @RequestParam String userName, @RequestParam String userPassword, MultipartFile userAvatar) {
        if (userServiceImpl.isRepeatUser(userName)) {
            return webResponse.getWebResponse(201, "用户名已存在，请重新注册！", null);
        }
        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("user_id", userId);
        paramMap.put("user_name", userName);
        if (userId == null || userId.length() == 0) {
            User user = userServiceImpl.addUser(userName, userPassword, userAvatar);
            if (user == null) {
                return webResponse.getWebResponse(201, "注册失败：用户名和密码必填", paramMap);
            }
            paramMap.put("user_avatar", user.getAvatarLink());
            return webResponse.getWebResponse(200, "注册成功", paramMap);
        } else {
            User user = userServiceImpl.updateUser(userId, userName, userPassword, userAvatar);
            if (user == null) {
                return webResponse.getWebResponse(201, "更新失败：不存在该用户", paramMap);
            }
            paramMap.put("user_avatar", user.getAvatarLink());
            return webResponse.getWebResponse(200, "更新成功", paramMap);
        }
    }

    @PostMapping("/getAllUser")
    @ResponseBody
    public WebResponse getAllUser(Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(16);
        List<User> userList = userServiceImpl.getAllUser(pageNo, pageSize);
        int total = userServiceImpl.getAllCount();
        map.put("list", userList);
        map.put("total", total);
        return webResponse.getWebResponse(200, "查询成功", map);
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public WebResponse deleteUser(@RequestBody List<String> userIds) {
        Map<String, Object> map = new HashMap<>(16);
        userServiceImpl.deleteUser(userIds);
        return webResponse.getWebResponse(200, "删除成功", null);
    }
}
