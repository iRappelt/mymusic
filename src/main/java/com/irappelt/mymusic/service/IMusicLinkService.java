package com.irappelt.mymusic.service;


import com.irappelt.mymusic.common.IServiceOperations;
import com.irappelt.mymusic.model.MusicLink;

import java.util.List;

public interface IMusicLinkService extends IServiceOperations<MusicLink, MusicLink> {

    /**
     * 将榜单音乐收藏插入到我的音乐表中
     */
    void insertSongCollection(int song_id, int userId);

    /**
     * 判断是否重复收藏
     */
    int judgeSong(String songName, int userId);

    /**
     * 从数据库中搜索歌曲
     */
    List<MusicLink> songSearch(String songName);

    int getUserId(String user_name, String user_password);
}
