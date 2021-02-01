package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.UserRepository;
import com.irappelt.mymusic.model.po.User;
import com.irappelt.mymusic.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(String userName, String userPassword) {
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        return userRepository.findOne(Example.of(user)).orElse(null);
    }

    @Override
    public User resetPassword(String userName, String newPassword) {
        User user = this.getUser(userName, null);
        user.setUserPassword(newPassword);
        return userRepository.save(user);
    }

    @Override
    public User addUser(String userName, String userPassword) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPassword)) {
            return null;
        }
        User user = new User();
        user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, String newUserName, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (StringUtils.isNotEmpty(newUserName)) {
                user.setUserName(newUserName);
            }
            if (StringUtils.isNotEmpty(newPassword)) {
                user.setUserPassword(newPassword);
            }
            user.setUpdateTime(new Date());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public boolean isRepeatUser(String userName) {
        return this.getUser(userName, null)!=null;
    }


}
