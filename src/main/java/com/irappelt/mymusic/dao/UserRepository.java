package com.irappelt.mymusic.dao;

import com.irappelt.mymusic.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:49
 */
public interface UserRepository extends JpaRepository<User, String> {
}
