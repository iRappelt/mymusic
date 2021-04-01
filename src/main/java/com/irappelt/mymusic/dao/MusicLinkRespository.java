package com.irappelt.mymusic.dao;


import com.irappelt.mymusic.model.po.MusicLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:48
 */
public interface MusicLinkRespository extends JpaRepository<MusicLink, String> {

    List<MusicLink> queryAllBySongNameContainingOrSingerContaining(String songName, String singer);

    @Query(value = "select count(songId) from MusicLink ;", nativeQuery = true)
    int getAllCount();

    @Query(value = "select * from MusicLink where SongId in (?1) order by field(SongId, ?1)", nativeQuery = true)
    List<MusicLink> getMusicListByIdList(List<String> songIdList);

    @Query(value = "SELECT m.* FROM MyMusic s, MusicLink m WHERE s.UserId=?1 AND s.SongId=m.SongId ORDER BY s.CreateTime ASC", nativeQuery = true)
    List<MusicLink> getMusicListByUserId(String userId);

    @Modifying
    @Query(value = "update MusicLink set PlayedNum = (PlayedNum + 1) where SongId = ?1", nativeQuery = true)
    int addPlayedNum(String songId);

    @Query(value = "select * from MusicLink order by PlayedNum desc, CreateTime desc limit ?1, ?2", nativeQuery = true)
    List<MusicLink> getHotMusicTop(int pageNo, int pageSize);

    @Query(value = "select * from MusicLink where CreateTime > DATE_SUB(NOW(),INTERVAL 7 day) order by PlayedNum desc, CreateTime desc limit ?1, ?2", nativeQuery = true)
    List<MusicLink> getRiseUpTop(int pageNo, int pageSize);

    @Query(value = "select * from MusicLink where CreateTime > DATE_SUB(NOW(),INTERVAL 3 day) order by PlayedNum desc, CreateTime asc limit ?1, ?2", nativeQuery = true)
    List<MusicLink> getNewMusicTop(int pageNo, int pageSize);

    @Query(value = "SELECT * FROM MusicLink WHERE TypeName IN (SELECT DISTINCT TypeName FROM MusicLink WHERE SongId IN (?1)) LIMIT 20", nativeQuery = true)
    List<MusicLink> queryRecommendMusic(List<String> songIds);
}
