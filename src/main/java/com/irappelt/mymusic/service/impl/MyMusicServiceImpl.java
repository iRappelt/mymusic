package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.MyMusicRepository;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.service.MyMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:28
 */
@Service
public class MyMusicServiceImpl implements MyMusicService {

    @Autowired
    private MyMusicRepository myMusicRepository;

    @Override
    public MyMusic addToMyMusic(MyMusic myMusic) {
        myMusic.setCollectId(UUID.randomUUID().toString().replaceAll("-", ""));

        myMusic.setCreateTime(new Date());
        myMusic.setUpdateTime(new Date());
        myMusic.setPlayedNum(0);
        return myMusicRepository.save(myMusic);
    }

    @Override
    public List<MyMusic> getMyMusicList(String userId) {
        return myMusicRepository.queryAllByUserIdOrderByCreateTimeAsc(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteMyMusic(String songId, String userId) {
        return myMusicRepository.deleteBySongIdAndUserId(songId, userId);
    }

    @Override
    public boolean myMusicIsRepeat(String songId, String userId) {
        MyMusic myMusic = new MyMusic();
        myMusic.setSongId(songId);
        myMusic.setUserId(userId);
        return myMusicRepository.findOne(Example.of(myMusic)).orElse(null) != null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addPlayedNum(String userId, String songId) {
        return myMusicRepository.addPlayedNum(userId, songId);
    }
}
