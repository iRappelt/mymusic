package com.irappelt.mymusic.service;

import com.irappelt.mymusic.dao.MyMusicRepository;
import com.irappelt.mymusic.model.po.MyMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return myMusicRepository.save(myMusic);
    }
}
