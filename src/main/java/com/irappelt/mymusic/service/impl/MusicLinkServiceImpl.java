package com.irappelt.mymusic.service.impl;

import com.irappelt.mymusic.common.WebResponse;
import com.irappelt.mymusic.dao.MusicLinkRespository;
import com.irappelt.mymusic.dao.MyMusicRepository;
import com.irappelt.mymusic.model.po.MusicLink;
import com.irappelt.mymusic.model.po.MyMusic;
import com.irappelt.mymusic.service.MusicLinkService;
import com.irappelt.mymusic.util.OssStorage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:51
 */
@Service
public class MusicLinkServiceImpl implements MusicLinkService {

    @Autowired
    private MusicLinkRespository musicLinkRespository;

    @Autowired
    private MyMusicRepository myMusicRepository;


    @Override
    public List<MusicLink> songSearch(String condition) {
        return musicLinkRespository.queryAllBySongNameContainingOrSingerContaining(condition, condition);
    }

    @Override
    public List<MusicLink> getMusicList(int pageNo, int pageSize, Integer topType) {

        int pageNum = (pageNo - 1) * pageSize;

        /*Sort sort;
        if (isAsc) {
            sort = Sort.by(Sort.Direction.ASC, orderField);
        } else {
            sort = Sort.by(Sort.Direction.DESC, orderField);
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<MusicLink> all = musicLinkRespository.findAll(pageable);*/
        if (topType == 1) {
            return musicLinkRespository.getRiseUpTop(pageNum, pageSize);
        } else if (topType == 2) {
            return musicLinkRespository.getHotMusicTop(pageNum, pageSize);
        } else if (topType == 3) {
            return musicLinkRespository.getNewMusicTop(pageNum, pageSize);
        }
        return null;
    }

    @Override
    public int getAllCount() {
        return musicLinkRespository.getAllCount();
    }

    @Override
    public List<MusicLink> getMusicListByIdList(List<String> songIdList) {
        return musicLinkRespository.getMusicListByIdList(songIdList);
    }

    @Override
    public MusicLink addMusicLink(MusicLink musicLink, String imageFormat, MultipartFile songImage, MultipartFile songFile, MultipartFile songLyric) {
        musicLink.setSongId(createSongId(musicLink.getSongName()));
        musicLink.setCollectedNum(0);
        musicLink.setCreateTime(new Date());
        musicLink.setUpdateTime(new Date());
        musicLink.setMvLink("");
        musicLink.setPlayedNum(0);
        musicLink.setSongTime("");
        musicLink.setSingerId("");
        musicLink.setPriority(1);
        musicLink.setUploaderId("1");
        musicLink.setUploaderName("官方");
        // 调用OSS存储
        Map<String, String> result = OssStorage.multipartFileUpload(musicLink.getSongName(), imageFormat, songImage, songFile, songLyric);
        musicLink.setImageLink(result.get("songImageUrl"));
        musicLink.setSongLink(result.get("songFileUrl"));
        if (StringUtils.isNotEmpty(result.get("songLyricUrl"))) {
            musicLink.setLyricLink(result.get("songLyricUrl"));
        } else {
            musicLink.setLyricLink("");
        }
        return musicLinkRespository.save(musicLink);
    }

    @Override
    public List<MusicLink> getMusicByCondition(MusicLink musicLink) {
        return musicLinkRespository.findAll(Example.of(musicLink));
    }

    @Override
    public String download(String songUrl) {
        return OssStorage.multipartFileDownload(songUrl);
    }

    @Override
    public String getSongLyric(String lyricLink) {
        return OssStorage.getSongLyric(lyricLink);
    }

    @Override
    public List<MusicLink> getMusicListByUserId(String userId) {
        return musicLinkRespository.getMusicListByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addPlayedNum(String songId) {
        return musicLinkRespository.addPlayedNum(songId);
    }

    @Override
    public List<MusicLink> getRecommendMusic(String userId) {

        List<MyMusic> myMusics = myMusicRepository.queryRecommendMusic(userId);
        if (myMusics != null && myMusics.size() > 0) {
            List<String> songIds = myMusics.stream().map(MyMusic::getSongId).collect(Collectors.toList());
            return musicLinkRespository.queryRecommendMusic(songIds);
        }
        return null;

    }


    /**
     * 生成songId
     *
     * @param songName 文件原始名
     * @return 当前时间格式yyMMddHHmmsss+原始文件名的base64编码
     */
    private static String createSongId(String songName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String part1 = sdf.format(date);
        String part2 = Base64.getEncoder().encodeToString(songName.getBytes()).replaceAll("/", "a");
        return part1 + part2;
    }
}
