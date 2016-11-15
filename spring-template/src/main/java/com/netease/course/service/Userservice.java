package com.netease.course.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import com.netease.course.meta.Product;
import com.netease.course.meta.User;

public interface Userservice {
	public User find(String userName,String password);
	
	public Product getproduct(int id);

	public void publiccontent(HttpServletRequest rq,boolean edit) throws SerialException, UnsupportedEncodingException, SQLException;

	public Product getlastproduct();
	
	public List<Product>getproductlist();
	
	public void buy(Product product,int id);
	
	public List<Product>getbuylist(int personId);
	
	public void deleteproduct(int id);
}
