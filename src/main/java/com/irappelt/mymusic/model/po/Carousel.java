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
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/04/01 23:23
 * @version: v1.0
 */
@Data
@Entity
@Table(name = "Carousel")
public class Carousel {
    @Id
    @Column(name = "CarouselId")
    private String carouselId;

    @Column(name = "Description")
    private String description;

    @Column(name = "SongIds")
    private String songIds;

    @Column(name = "PicLinks")
    private String picLinks;

    @Column(name = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
