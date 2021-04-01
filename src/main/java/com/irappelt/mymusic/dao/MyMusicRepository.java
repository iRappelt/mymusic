package com.irappelt.mymusic.dao;


import com.irappelt.mymusic.model.po.MyMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:19
 */
public interface MyMusicRepository extends JpaRepository<MyMusic, String> {
    int deleteBySongIdAndUserId(String songId, String userId);

    List<MyMusic> queryAllByUserIdOrderByCreateTimeAsc(String userId);

    @Modifying
    @Query(value = "update MyMusic set PlayedNum = (PlayedNum + 1) where UserId = ?1 and SongId = ?2", nativeQuery = true)
    int addPlayedNum(String userId, String songId);

    @Query(value = "select * from MyMusic WHERE UserId=?1 ORDER BY PlayedNum DESC limit 20", nativeQuery = true)
    List<MyMusic> queryRecommendMusic(String userId);

}
