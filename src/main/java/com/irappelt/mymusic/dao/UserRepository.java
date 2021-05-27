package com.irappelt.mymusic.dao;

import com.irappelt.mymusic.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:49
 */
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select * from User order by CreateTime desc limit ?1, ?2", nativeQuery = true)
    List<User> getAllUser(Integer pageNo, Integer pageSize);

    @Query(value = "select count(*) from User", nativeQuery = true)
    int getAllCount();
}
