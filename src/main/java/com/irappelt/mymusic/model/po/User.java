package com.irappelt.mymusic.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: huaiyu
 * @date: Created in 2021/1/29 13:45
 */
@Data
@Entity
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames="UserName")})
public class User {
    @Id
    @Column(name = "UserId")
    private String userId;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "UserPassword")
    private String userPassword;

    @Column(name = "CreateTime")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "UpdateTime")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "AvatarLink")
    private String avatarLink;
}
