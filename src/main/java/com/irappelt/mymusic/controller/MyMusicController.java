package com.irappelt.mymusic.controller;

import java.util.*;

import javax.annotation.Resource;

import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.model.po.User;
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
public class MyMusicController {

    @Autowired
    protected WebResponse webResponse;

    @Resource
    protected MyMusicService myMusicServiceImpl;

    @Resource
    protected UserService userServiceImpl;

    /**
     * 从数据库中获取歌曲数据，在我的音乐中显示
     */
    @RequestMapping(value = "/getMyMusicList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse getMyMusicList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                      @RequestParam(defaultValue = "正常", required = false) String tbStatus,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "ml_id", required = false) String order,
                                      @RequestParam(defaultValue = "desc", required = false) String desc,
                                      @RequestParam(required = false) String user_name, @RequestParam(required = false) String user_password
    ) {

        Map<String, Object> map = new HashMap<>(16);
        User user = userServiceImpl.getUser(user_name, user_password);
        List<MyMusic> list = this.myMusicServiceImpl.getMyMusicList(user.getUserId());
        map.put("list", list);
        if (list != null && list.size() > 0) {
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
    public WebResponse deleteMyMusic(@RequestParam(required = false) String user_id, @RequestParam(required = false) String song_id) {

        WebResponse webResponse = new WebResponse();

        if (user_id == null || song_id == null) {
            return webResponse.getWebResponse(201, "删除失败:user_id/song_id未传", null);
        }
        int del = this.myMusicServiceImpl.deleteMyMusic(song_id, user_id);
        if (del > 0) {
            return webResponse.getWebResponse(200, "删除成功", null);
        }
        return webResponse.getWebResponse(201, "删除失败", null);
    }

}
