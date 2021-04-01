package com.irappelt.mymusic.service;


import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.MusicLink;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
     * @param topType
     * @return
     */
    List<MusicLink> getMusicList(int pageNo, int pageSize, Integer topType);

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
    MusicLink addMusicLink(MusicLink musicLink, String imageFormat, MultipartFile songImage, MultipartFile songFile, MultipartFile songLyric);

    /**
     * 根据条件获得歌曲列表
     * @param musicLink
     * @return
     */
    List<MusicLink> getMusicByCondition(MusicLink musicLink);

    String download(String songUrl);

    String getSongLyric(String lyricLink);

    /**
     * 根据用户id查询用户收藏的歌曲信息
     * @param userId
     * @return
     */
    List<MusicLink> getMusicListByUserId(String userId);

    /**
     * 将播放次数加1
     * @param songId 歌曲id
     * @return
     */
    int addPlayedNum(String songId);

    /**
     * 个性化推荐我的音乐
     * @param userId
     * @return
     */
    List<MusicLink> getRecommendMusic(String userId);
}
