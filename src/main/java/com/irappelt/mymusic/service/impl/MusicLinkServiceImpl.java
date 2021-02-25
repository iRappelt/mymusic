package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.dao.MusicLinkRespository;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.service.MusicLinkService;
import com.irappelt.mymusic.util.OssStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
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
    public MusicLink addMusicLink(MusicLink musicLink, String imageFormat, MultipartFile songImage, MultipartFile songFile) {
        musicLink.setSongId(UUID.randomUUID().toString().replaceAll("-", ""));
        musicLink.setCollectedNum(0);
        musicLink.setCreateTime(new Date());
        musicLink.setLyricLink("");
        musicLink.setMvLink("");
        musicLink.setPlayedNum(0);
        // 调用OSS存储
        Map<String, String> result = OssStorage.multipartFileUpload(imageFormat, songImage, songFile);
        musicLink.setImageLink(result.get("songImageUrl"));
        musicLink.setSongLink(result.get("songFileUrl"));
        return musicLinkRespository.save(musicLink);
    }
}
