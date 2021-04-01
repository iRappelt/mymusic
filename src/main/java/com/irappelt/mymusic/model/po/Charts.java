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
 * @date: 2021/03/28 17:13
 * @version: v1.0
 */
@Data
@Entity
@Table(name = "Charts")
public class Charts {
    @Id
    @Column(name = "ChartsId")
    private String chartsId;

    @Column(name = "BlockA")
    private String blockA;

    @Column(name = "BlockB")
    private String blockB;

    @Column(name = "BlockC")
    private String blockC;

    @Column(name = "BlockD")
    private String blockD;

    @Column(name = "BlockE")
    private String blockE;

    @Column(name = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
