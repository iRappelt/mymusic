package com.irappelt.mymusic.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.aop.exception.ParamVerifyException;
import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.enums.SongTypeEnum;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.model.po.User;
import com.irappelt.mymusic.service.MusicLinkService;
import com.irappelt.mymusic.service.MyMusicService;
import com.irappelt.mymusic.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huaiyu
 */
@Controller
@RequestMapping("/musicLink")
@ExceptionCapture
public class MusicLinkController {

    @Autowired
    private WebResponse webResponse;

    @Autowired
    private MusicLinkService musicLinkServiceImpl;

    @Resource
    private UserService userServiceImpl;

    @Resource
    private MyMusicService myMusicServiceImpl;


	/**
	 * 从数据库中获取歌曲数据，在榜单中显示
	 */
	@RequestMapping(value = "/getMusicLinkList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getMusicLinkList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(defaultValue = "20", required = false) Integer pageSize, Integer topType) {

        Map<Object, Object> map = new HashMap<>(16);

        int count = musicLinkServiceImpl.getAllCount();
        List<MusicLink> list = musicLinkServiceImpl.getMusicList(pageNo, pageSize, topType);
        if (list != null && list.size() > 0) {
            map.put("total", count);
            map.put("list", list);
            return webResponse.getWebResponse(200, "根据条件获取分页数据成功", map);
        } else {
            map.put("total", 0);
            map.put("list", list);
            return webResponse.getWebResponse(202, "no record!", map);
        }
    }

    /**
     * 歌曲搜索功能
     */
    @RequestMapping(value = "/getSongSearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getSongSearch(String songName) {

        Optional.ofNullable(songName).orElseThrow(() -> new ParamVerifyException("搜索歌曲名不允许为空"));

        Map<Object, Object> map = new HashMap<>(16);

        List<MusicLink> list = this.musicLinkServiceImpl.songSearch(songName);
        map.put("total", list.size());
        map.put("list", list);
        if (list.size() > 0) {
            return webResponse.getWebResponse(200, "根据条件获取分页数据成功", map);
        } else {
            return webResponse.getWebResponse(202, "no record!", map);
        }
    }

    /**
     * 歌曲收藏
     */
    @RequestMapping(value = "/addMusicCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addMusicCollect(@RequestParam("song_id") String songId, @RequestParam("user_id") String userId) {

        if (StringUtils.isEmpty(songId)) {
            throw new ParamVerifyException("songId为空");
        }

        WebResponse webResponse = new WebResponse();

        boolean isRepeat = myMusicServiceImpl.myMusicIsRepeat(songId, userId);
        if (isRepeat) {
            return webResponse.getWebResponse(201, "已收藏,请不要重复收藏！", null);
        } else {
            MyMusic myMusic = new MyMusic();
            myMusic.setUserId(userId);
            myMusic.setSongId(songId);
            myMusicServiceImpl.addToMyMusic(myMusic);
            return webResponse.getWebResponse(200, "收藏成功", null);
        }
    }

    /**
     * 歌曲上传
     */
    @RequestMapping(value = "/addMusicLink", method = RequestMethod.POST)
    @ResponseBody
    public WebResponse addMusicLink(String songName, String singer, String[] songType, String imageFormat, MultipartFile songImage, MultipartFile songFile, MultipartFile songLyric) {
        if (StringUtils.isEmpty(songName) || StringUtils.isEmpty(singer) || StringUtils.isEmpty(imageFormat) || StringUtils.isEmpty(Arrays.toString(songType))) {
            throw new ParamVerifyException("传递的参数为{songName=" + songName + ",singer=" + singer + ",imageFormat=" + imageFormat + ",songType" + Arrays.toString(songType) +"}");
        }
        Optional.ofNullable(songImage).orElseThrow(() -> new ParamVerifyException("歌曲图片不允许为空"));
        Optional.ofNullable(songFile).orElseThrow(() -> new ParamVerifyException("歌曲文件不允许为空"));
        // 歌词可以不传

        MusicLink musicLink = new MusicLink();
        musicLink.setSongName(songName);
        musicLink.setSinger(singer);
        // 歌曲已经存在校验：通过歌曲名和歌手名校验
        List<MusicLink> list = musicLinkServiceImpl.getMusicByCondition(musicLink);
        if (list != null && list.size() > 0) {
            return webResponse.getWebResponse(203, "上传失败,歌曲已经存在", list);
        }
        musicLink.setTypeId(String.join("|",songType));
        List<String> typeNameList = new ArrayList<>();
        for (String typeId: songType) {
            typeNameList.add(SongTypeEnum.getTypeName(Integer.parseInt(typeId)));
        }
        musicLink.setTypeName(String.join("|",typeNameList));
        // 上传
        MusicLink music = musicLinkServiceImpl.addMusicLink(musicLink, imageFormat, songImage, songFile, songLyric);

        if (music == null) {
            return webResponse.getWebResponse(201, "上传失败", null);
        }
        return webResponse.getWebResponse(200, "上传成功", music);
    }


    /**
     * 歌曲下载
     * @param songUrl
     * @return
     */
    @GetMapping("/download")
    @ResponseBody
    public Map<String, Object> download(String songUrl) {
        Map<String, Object> map = new HashMap<>(16);
        String[] strArray = songUrl.split("/");
        String fileName = strArray[strArray.length - 1];
        map.put("file", musicLinkServiceImpl.download(songUrl));
        map.put("fileName", fileName);
        return map;
    }

    /**
     * 获取歌词文件
     * @param lyricLink
     * @return
     */
    @GetMapping("/lyric")
    @ResponseBody
    public String getSongLyric(String lyricLink) {

        return musicLinkServiceImpl.getSongLyric(lyricLink);
    }

    /**
     * 每次播放将音乐表对应音乐播放次数加1，并且如果该歌曲已被收藏的话，收藏的音乐表对应音乐播放次数也要加1
     * @param songId
     * @param userId
     */
    @RequestMapping(value = "/addPlayedNum", method = RequestMethod.POST)
    @ResponseBody
    public WebResponse addPlayedNum(String songId, String userId) {
        int result1 = musicLinkServiceImpl.addPlayedNum(songId);
        int result2 = myMusicServiceImpl.addPlayedNum(userId, songId);

        if (result1 + result2 == 2) {
            return webResponse.getWebResponse(200, "增加成功", result1+"-"+result2);
        }
        return webResponse.getWebResponse(201, "增加失败", result1+"-"+result2);

    }


}
