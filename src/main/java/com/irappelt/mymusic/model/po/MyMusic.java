package com.irappelt.mymusic.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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

    @Column(name = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "PlayedNum")
    private Integer playedNum;
}
