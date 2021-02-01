package com.irappelt.mymusic.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.model.po.User;
import com.irappelt.mymusic.service.MusicLinkService;
import com.irappelt.mymusic.service.MyMusicService;
import com.irappelt.mymusic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huaiyu
 */
@Controller 
@RequestMapping("/musicLink")
public class MusicLinkController {

	@Autowired
	protected WebResponse webResponse;

	@Autowired
	protected MusicLinkService musicLinkServiceImpl;

	@Resource
	protected UserService userServiceImpl;

	@Resource
	protected MyMusicService myMusicServiceImpl;


	/**
	 * 从数据库中获取歌曲数据，在榜单中显示
	 */
	@RequestMapping(value = "/getMusicLinkList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getMusicLinkList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize,
			@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "ml_id", required = false) String order,
			@RequestParam(defaultValue = "desc", required = false) String desc) {

		Map<Object, Object> map = new HashMap<>(16);

		int count = musicLinkServiceImpl.getAllCount();
		map.put("total", count);
		List<MusicLink> list = musicLinkServiceImpl.getMusicList(pageNo, pageSize, null, false, order);
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
	@RequestMapping(value = "/getSongRearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getSongRearch(@RequestParam(required = false) String songName) {

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
	public WebResponse addMusicCollect(@RequestParam(required = false) String song_id, @RequestParam(required = false) String user_name,
			@RequestParam(required = false) String user_password, @RequestParam(required = false) String songName) {

		WebResponse webResponse = new WebResponse();

		User user = userServiceImpl.getUser(user_name, user_password);
		boolean isRepeat = myMusicServiceImpl.myMusicIsRepeat(song_id, user.getUserId());
		if (isRepeat) {
			return webResponse.getWebResponse(201, "已收藏,请不要重复收藏！", null);
		} else {
			MyMusic myMusic = new MyMusic();
			myMusic.setUserId(user.getUserId());
			myMusic.setSongId(song_id);
			myMusicServiceImpl.addToMyMusic(myMusic);
			return webResponse.getWebResponse(200, "收藏成功", null);
		}
	}
}
