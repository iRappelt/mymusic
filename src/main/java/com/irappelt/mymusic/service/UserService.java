package com.irappelt.mymusic.service;


import com.irappelt.mymusic.model.po.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:53
 */
public interface UserService {
    /**
     * 根据用户民和密码获得用户信息
     * @param userName
     * @param userPassword
     * @return
     */
    User getUser(String userName, String userPassword);

    User resetPassword(String userName, String newPassword);

    User addUser(String userName, String userPassword, MultipartFile userAvatar);

    User updateUser(String userId, String newUserName, String newPassword);

    boolean isRepeatUser(String userName);
}
