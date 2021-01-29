

package com.irappelt.mymusic.service;

import com.irappelt.mymusic.common.IServiceOperations;
import com.irappelt.mymusic.model.MyMusic;

import java.util.List;


public interface IMyMusicService extends IServiceOperations<MyMusic, MyMusic> {

	String getUserById(String user_name, String user_password);

	/**
	 * 从数据库中获取音乐到我的音乐列表中
	 */
	 List<MyMusic> getMyMusicList(int userId);

	/**
	 * 删除音乐
	 */
	int deleteMyMusic(int song_id, int user_id);

}
