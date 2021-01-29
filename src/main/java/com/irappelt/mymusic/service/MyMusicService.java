package com.irappelt.mymusic.service;

import com.irappelt.mymusic.model.po.MyMusic;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:26
 */
public interface MyMusicService {

    /**
     * 添加到我的收藏
     * @param myMusic
     * @return
     */
    MyMusic addToMyMusic(MyMusic myMusic);
}
