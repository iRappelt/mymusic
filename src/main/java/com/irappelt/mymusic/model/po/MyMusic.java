package com.irappelt.mymusic.model.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 10:05
 */
@Data
@Entity
@Table(name = "MyMusic")
public class MyMusic {

    @Id
    @Column(name = "KeyId")
    private String keyId;

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

    @Column(name = "UserId")
    private String userId;
}
