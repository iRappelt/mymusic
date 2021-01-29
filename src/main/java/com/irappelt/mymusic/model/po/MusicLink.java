package com.irappelt.mymusic.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 11:41
 */
@Data
@Entity
@Table(name = "MusicLink")
public class MusicLink {

    @Id
    @Column(name = "SongId")
    private String songId;

    @Column(name = "SongName")
    private String songName;

    @Column(name = "Singer")
    private String singer;

    @Column(name = "SongLink")
    private String songLink;

    @Column(name = "LyricLink")
    private String lyricLink;

    @Column(name = "ImageLink")
    private String imageLink;

    @Column(name = "MvLink")
    private String mvLink;

    @Column(name = "PlayedNum")
    private Integer playedNum;

    @Column(name = "CollectedNum")
    private Integer collectedNum;

    @Column(name = "CreateTime")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
