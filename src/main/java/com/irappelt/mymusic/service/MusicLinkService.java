package com.irappelt.mymusic.service;


import com.irappelt.mymusic.model.po.MusicLink;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:51
 */
public interface MusicLinkService {

    /**
     * 根据歌曲名/歌手/模糊搜索
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
    List<MusicLink> getMusicList(int pageNo, int pageSize, String keyword, boolean isAsc, String orderField);

    /**
     * 获取所有数据条数
     * @return
     */
    int getAllCount();
}
