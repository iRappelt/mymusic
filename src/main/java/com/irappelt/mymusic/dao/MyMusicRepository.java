package com.irappelt.mymusic.dao;

import com.irappelt.mymusic.model.po.MyMusic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:19
 */
public interface MyMusicRepository extends JpaRepository<MyMusic, String> {
}
