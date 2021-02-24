package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.MusicLinkRespository;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.service.MusicLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:51
 */
@Service
public class MusicLinkServiceImpl implements MusicLinkService {

    @Autowired
    private MusicLinkRespository musicLinkRespository;


    @Override
    public List<MusicLink> songSearch(String condition) {
        return musicLinkRespository.queryAllBySongNameContainingOrSingerContaining(condition, condition);
    }

    @Override
    public List<MusicLink> getMusicList(int pageNo, int pageSize, boolean isAsc, String orderField) {

        Sort sort;
        if (isAsc) {
            sort = Sort.by(Sort.Direction.ASC, orderField);
        } else {
            sort = Sort.by(Sort.Direction.DESC, orderField);
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<MusicLink> all = musicLinkRespository.findAll(pageable);
        return all.getContent();
    }

    @Override
    public int getAllCount() {
        return musicLinkRespository.getAllCount();
    }

    @Override
    public List<MusicLink> getMusicListByIdList(List<String> songIdList) {
        return musicLinkRespository.queryAllBySongIdIn(songIdList);
    }

    @Override
    public MusicLink addMusicLink(MusicLink musicLink) {
        return musicLinkRespository.save(musicLink);
    }
}
