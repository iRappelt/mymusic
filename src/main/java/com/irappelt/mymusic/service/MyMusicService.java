package com.irappelt.mymusic.service;


import com.irappelt.mymusic.model.po.MyMusic;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:58
 */
public interface MyMusicService {
    /**
     * 添加到我的音乐收藏
     * @param myMusic
     * @return
     */
    MyMusic addToMyMusic(MyMusic myMusic);

    /**
     * 得到我的收藏列表
     * @param userId
     * @return
     */
    List<MyMusic> getMyMusicList(String userId);

    /**
     * 从我的收藏删除歌曲
     * @param songId
     * @return
     */
    int deleteMyMusic(String songId, String userId);

    boolean myMusicIsRepeat(String songId, String userId);
}
