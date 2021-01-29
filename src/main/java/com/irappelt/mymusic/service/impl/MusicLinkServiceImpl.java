/** 
* 
*
*
*/

package com.irappelt.mymusic.service.impl;

import java.util.List;

import javax.annotation.Resource;


import com.irappelt.mymusic.common.AbstractService;
import com.irappelt.mymusic.common.IOperations;
import com.irappelt.mymusic.mapper.IMusicLinkMapper;
import com.irappelt.mymusic.model.MusicLink;
import com.irappelt.mymusic.service.IMusicLinkService;
import org.springframework.stereotype.Service;


@Service("musicLinkServiceImpl")
public class MusicLinkServiceImpl extends AbstractService<MusicLink, MusicLink> implements IMusicLinkService {

	public MusicLinkServiceImpl() {
		this.setTableName("musiclink");
	}

	@Resource
	private IMusicLinkMapper musicLinkMapper;

	@Override
	protected IOperations<MusicLink, MusicLink> getMapper() {
		return musicLinkMapper;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	@Override
	public List<MusicLink> songSearch(String songName) {
		return musicLinkMapper.songSearch(songName);
	}

	@Override
	public int getUserId(String user_name, String user_password) {
		return musicLinkMapper.getUserId(user_name, user_password);
	}

	@Override
	public void insertSongCollection(int song_id, int userId) {
		musicLinkMapper.insertSongCollection(song_id, userId);
	}

	@Override
	public int judgeSong(String songName, int userId) {
		return musicLinkMapper.judgeSong(songName, userId);
	}

	@Override
	public void insert(MusicLink entity) {

	}
}
