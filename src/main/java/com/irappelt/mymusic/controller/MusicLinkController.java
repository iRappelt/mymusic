package com.irappelt.mymusic.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.irappelt.mymusic.aop.annotation.ExceptionCapture;
import com.irappelt.mymusic.aop.exception.ParamVerifyException;
import com.irappelt.mymusic.common.WebResponse;
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
			@RequestParam(defaultValue = "15", required = false) Integer pageSize,
			@RequestParam(defaultValue = "songId", required = false) String order) {

        Map<Object, Object> map = new HashMap<>(16);

        int count = musicLinkServiceImpl.getAllCount();
        map.put("total", count);
        List<MusicLink> list = musicLinkServiceImpl.getMusicList(pageNo, pageSize, false, order);
        if (list != null && list.size() > 0) {
            map.put("list", list);
            return webResponse.getWebResponse(200, "根据条件获取分页数据成功", map);
        } else {
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
    public WebResponse addMusicCollect(@RequestParam("song_id") String songId, @RequestParam("user_name") String userName,
                                       @RequestParam("user_password") String userPassword) {

        if (StringUtils.isEmpty(songId) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPassword)) {
            throw new ParamVerifyException("传递的参数为{songID=" + songId + ",userName=" + userName + ",userPassword=" + userPassword + "}");
        }

        WebResponse webResponse = new WebResponse();

        User user = userServiceImpl.getUser(userName, userPassword);
        boolean isRepeat = myMusicServiceImpl.myMusicIsRepeat(songId, user.getUserId());
        if (isRepeat) {
            return webResponse.getWebResponse(201, "已收藏,请不要重复收藏！", null);
        } else {
            MyMusic myMusic = new MyMusic();
            myMusic.setUserId(user.getUserId());
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
    public WebResponse addMusicLink(String songName, String singer, String imageFormat, MultipartFile songImage, MultipartFile songFile, MultipartFile songLyric) {
        if (StringUtils.isEmpty(songName) || StringUtils.isEmpty(singer) || StringUtils.isEmpty(imageFormat)) {
            throw new ParamVerifyException("传递的参数为{songName=" + songName + ",singer=" + singer + ",imageFormat=" + imageFormat + "}");
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
}
