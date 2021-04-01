package com.irappelt.mymusic.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.aop.exception.ParamVerifyException;
import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.model.po.User;
import com.irappelt.mymusic.service.MusicLinkService;
import com.irappelt.mymusic.service.MyMusicService;
import com.irappelt.mymusic.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @date 2018年12月23日 00:03:48
 */

@Controller
@RequestMapping("/myMusic")
@ExceptionCapture
public class MyMusicController {

    @Autowired
    private WebResponse webResponse;

    @Resource
    private MyMusicService myMusicServiceImpl;

    @Resource
    private UserService userServiceImpl;

    @Autowired
    private MusicLinkService musicLinkServiceImpl;

    /**
     * 从数据库中获取歌曲数据，在我的音乐中显示
     */
    @RequestMapping(value = "/getMyMusicList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getMyMusicList(@RequestParam(value = "user_id") String userId) {

        Optional.ofNullable(userId).orElseThrow(() -> new ParamVerifyException("用户Id不允许为空"));

        Map<String, Object> map = new HashMap<>(16);
        List<MusicLink> musicList = this.musicLinkServiceImpl.getMusicListByUserId(userId);
        map.put("list", musicList);
        if (musicList.size() > 0) {
            return webResponse.getWebResponse(200, "根据条件获取分页数据成功", map);
        } else {
            return webResponse.getWebResponse(202, "no record!", map);
        }
    }

    /**
     * 删除我收藏的音乐
     */
    @RequestMapping(value = "/deleteMyMusic", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse deleteMyMusic(@RequestParam("user_id") String userId, @RequestParam("song_id") String songId) {

        WebResponse webResponse = new WebResponse();

        if (userId == null || songId == null) {
            return webResponse.getWebResponse(201, "删除失败:user_id/song_id未传", null);
        }
        int del = this.myMusicServiceImpl.deleteMyMusic(songId, userId);
        if (del > 0) {
            return webResponse.getWebResponse(200, "删除成功", null);
        }
        return webResponse.getWebResponse(201, "删除失败", null);
    }

    /**
     * 校验歌曲是否在我的收藏列表
     */
    @RequestMapping(value = "/isCollected", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse isCollected(@RequestParam("user_id") String userId, @RequestParam("song_id") String songId) {

        WebResponse webResponse = new WebResponse();

        if (userId == null || songId == null) {
            return webResponse.getWebResponse(201, "查询失败:user_id/song_id未传", null);
        }
        boolean isCollected = this.myMusicServiceImpl.myMusicIsRepeat(songId, userId);
        if (isCollected) {
            return webResponse.getWebResponse(200, "歌曲已收藏", null);
        }
        return webResponse.getWebResponse(201, "歌曲未收藏", null);
    }


    /**
     * 获得推荐音乐
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "/getRecommendMusic", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getRecommendMusic(String userId) {
        List<MusicLink> recommendMusic = musicLinkServiceImpl.getRecommendMusic(userId);
        Map<String, Object> map = new HashMap<>(16);
        map.put("list", recommendMusic);
        if (recommendMusic != null && recommendMusic.size() > 0) {
            return webResponse.getWebResponse(200, "获取推荐音乐成功", map);
        } else {
            return webResponse.getWebResponse(202, "获取推荐音乐失败", map);
        }
    }

}
