package com.irappelt.mymusic.model;

import lombok.Data;

/**
 * @author huaiyu
 */
@Data
public class User implements java.io.Serializable {
	public Integer user_id;
	public String user_name;
	public String user_password;
}
