package com.irappelt.mymusic.service;


import com.irappelt.mymusic.model.po.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:53
 */
public interface UserService {
    /**
     * 根据用户名和密码获得用户信息
     * @param userName
     * @param userPassword
     * @return
     */
    User getUser(String userName, String userPassword);

    User resetPassword(String userName, String newPassword);

    User addUser(String userName, String userPassword, MultipartFile userAvatar);

    User updateUser(String userId, String newUserName, String newPassword, MultipartFile userAvatar);

    boolean isRepeatUser(String userName);

    List<User> getAllUser(Integer pageNo, Integer pageSize);

    int getAllCount();

    void deleteUser(List<String> userIds);
}
