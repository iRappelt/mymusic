package com.irappelt.mymusic.service;


import com.irappelt.mymusic.model.po.MusicLink;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:51
 */
public interface MusicLinkService {

    /**
     * 根据 歌曲名/歌手 模糊搜索
     * @param condition condition
     * @return List<MusicLink>
     */
    List<MusicLink> songSearch(String condition);

    /**
     * 获取榜单
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param isAsc
     * @param orderField
     * @return
     */
    List<MusicLink> getMusicList(int pageNo, int pageSize, boolean isAsc, String orderField);

    /**
     * 获取所有数据条数
     * @return
     */
    int getAllCount();

    /**
     * 根据歌曲ID列表获得歌曲信息列表
     * @param songIdList
     * @return
     */
    List<MusicLink> getMusicListByIdList(List<String> songIdList);

    /**
     * 添加歌曲信息
     * @param musicLink
     * @return
     */
    MusicLink addMusicLink(MusicLink musicLink);
}
