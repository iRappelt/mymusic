package com.irappelt.mymusic.mapper;

import com.irappelt.mymusic.common.IOperations;
import com.irappelt.mymusic.model.MusicLink;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IMusicLinkMapper extends IOperations<MusicLink, MusicLink> {

	/**
	 * #是将传入的值当做字符串的形式,$是将传入的数据直接显示生成sql语句,所以必须用$
	 * 从数据库中搜索歌曲
	 */
	@Select("select * from musiclink where ml_songName like '%${songName}%'")
	List<MusicLink> songSearch(@Param("songName") String songName);

	/**
	 * 获取用户id
	 */
	@Select("select user_id from user where user_name=#{user_name} and user_password=#{user_password}")
	int getUserId(@Param("user_name") String user_name, @Param("user_password") String user_password);

	/**
	 * 添加收藏
	 */
	void insertSongCollection(int song_id, int userId);

	/**
	 * 判断歌曲重复
	 */
	@Select("select my_id from mymusic where my_songName=#{songName} and user_id =#{userId}")
	int judgeSong(@Param("songName") String songName, @Param("userId") int userId);

}
