package com.irappelt.mymusic.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.model.MusicLink;
import com.irappelt.mymusic.service.IMusicLinkService;
import com.irappelt.mymusic.service.impl.MusicLinkServiceImpl;
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

	@Resource
	protected IMusicLinkService musicLinkService;

	@Resource
	protected MusicLinkServiceImpl musicLinkServiceImpl;

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

		LinkedHashMap<String, String> condition = new LinkedHashMap<>();
		Map<Object, Object> map = new HashMap<>(16);
		if (keyword != null && keyword.length() > 0) {
			String buf = "(test_name like '%" + keyword + "%' or info like '%" + keyword + "%' or other like '%" + keyword + "%')";
			condition.put(buf, "and");
		}
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.musicLinkService.getCount(condition, null);
		map.put("total", count);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<MusicLink> list = this.musicLinkService.getList(condition, pageNo, pageSize, order, null);
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
		if (list.size() > 0) {
			map.put("list", list);
			return webResponse.getWebResponse(200, "根据条件获取分页数据成功", map);
		} else {
			map.put("list", list);
			return webResponse.getWebResponse(202, "no record!", map);
		}
	}

	/**
	 * 歌曲收藏
	 */
	@RequestMapping(value = "/addMusicCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addMusicCollect(@RequestParam(required = false) int song_id, @RequestParam(required = false) String user_name,
			@RequestParam(required = false) String user_password, @RequestParam(required = false) String songName) {

		WebResponse webResponse = new WebResponse();

		String statusMsg = "";
		int statusCode = 200;
		int userId = musicLinkServiceImpl.getUserId(user_name, user_password);
		int myId = this.musicLinkService.judgeSong(songName, userId);
		if (myId > 0) {
			statusCode = 201;
			statusMsg = "已收藏,请不要重复收藏！";
		} else {
			this.musicLinkService.insertSongCollection(song_id, userId);
		}
		return webResponse.getWebResponse(statusCode, statusMsg, null);
	}
}
