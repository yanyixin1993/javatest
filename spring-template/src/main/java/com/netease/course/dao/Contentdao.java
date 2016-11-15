package com.netease.course.dao;

import java.sql.Blob;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.databind.JavaType;
import com.netease.course.meta.Content;
import com.netease.course.meta.Product;

public interface Contentdao {
	@Insert("INSERT INTO content (price,title,icon,abstract,text) VALUES (#{0},#{1},#{2},#{3},#{4})")
	public void insert(long price,String title,Blob image,
			String _abstract,Blob text);
	
	@Results({ 
		@Result(property = "id", column = "id"), 
		@Result(property = "price", column = "price"),
		@Result(property = "title", column = "title"),
		@Result(property = "image", column = "icon",jdbcType=JdbcType.BLOB),
		@Result(property = "summary", column = "abstract"),
		@Result(property = "text", column = "text",jdbcType=JdbcType.BLOB)}
	)
	@Select("SELECT * FROM content WHERE(id = #{0})")
	public Product select(int id);
	@Select("SELECT LAST_INSERT_ID()")
	public int selectlastId();
	@Update("Update content SET price=#{0},title=#{1},icon=#{2},abstract=#{3},text=#{4} where id=#{5}")
	public void update(long price, String title, Blob imageBlob, String summary, Blob detailBlob,int id);
	@Results({ 
		@Result(property = "id", column = "id"), 
		@Result(property = "price", column = "price"),
		@Result(property = "title", column = "title"),
		@Result(property = "image", column = "icon",jdbcType=JdbcType.BLOB),
		@Result(property = "summary", column = "abstract"),
		@Result(property = "text", column = "text",jdbcType=JdbcType.BLOB)}
	)
	@Select("SELECT * FROM content")
	public List<Product>getproductlist();
	@Delete("DELETE FROM content WHERE (id=#{0})")
	public void delete(int id);
}
