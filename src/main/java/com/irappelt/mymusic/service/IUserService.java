/** 
* 
*
*
*/

package com.irappelt.mymusic.service;


import com.irappelt.mymusic.common.IServiceOperations;
import com.irappelt.mymusic.model.User;

public interface IUserService extends IServiceOperations<User, User> {

	// 判断用户名是否重复
	public String rearchUserName(String user_name);
}
