package com.irappelt.mymusic.model;


import lombok.Data;

/**
 * @author huaiyu
 */
@Data
public class MyMusic implements java.io.Serializable {
	private Integer my_id;
	private String my_songName;
	private String my_singer;
	private String my_songLink;
	private String my_lyricLink;
	private String my_photoLink;
	private Integer user_id;
}
