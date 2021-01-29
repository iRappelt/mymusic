package com.irappelt.mymusic.dao;


import com.irappelt.mymusic.model.po.MusicLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:48
 */
public interface MusicLinkRespository extends JpaRepository<MusicLink, String> {

    List<MusicLink> queryAllBySongNameContainingOrSingerContaining(String songName, String Singer);
}
