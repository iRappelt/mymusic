package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.MyMusicRepository;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.service.MyMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return myMusicRepository.save(myMusic);
    }

    @Override
    public List<MyMusic> getMyMusicList(String userId) {
        MyMusic myMusic = new MyMusic();
        myMusic.setUserId(userId);
        return myMusicRepository.findAll(Example.of(myMusic));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
}
