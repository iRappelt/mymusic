package com.irappelt.mymusic.model;

import lombok.Data;

/**
 * @author huaiyu
 */
@Data
public class MusicLink implements java.io.Serializable {
	private Integer ml_id;
	private String ml_songName;
	private String ml_singer;
	private String ml_songLink;
	private String ml_lyricLink;
	private String ml_photoLink;
}
