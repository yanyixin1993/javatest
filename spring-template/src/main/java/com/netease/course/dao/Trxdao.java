package com.netease.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.netease.course.meta.Trx;

public interface Trxdao {
	@Select("SELECT * from trx")
	List<Trx>gettrxlist();
	@Select("SELECT * from trx where contentId=#{0}")
	public Trx selectid(int id);
	@Insert("INSERT INTO trx (contentid,personId,price,time) VALUES (#{0},#{1},#{2},#{3})")
	public void insert(int contentid,int personid,float price,long time);
	@Select("SELECT * from trx where personId=#{0}")
	public List<Trx> selectpersonid(int personId);
}
