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
    @Column(name = "CollectId")
    private String collectId;

    @Column(name = "UserId")
    private String userId;

    @Column(name = "SongId")
    private String songId;
}
