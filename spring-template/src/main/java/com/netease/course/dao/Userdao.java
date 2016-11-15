package com.netease.course.dao;


import org.apache.ibatis.annotations.Select;

import com.netease.course.meta.User;

public interface Userdao {
	@Select("SELECT * FROM person WHERE(username = #{0} AND password = #{1})")
	public User find(String userName,String password);
}
