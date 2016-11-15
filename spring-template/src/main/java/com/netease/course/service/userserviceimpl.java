package com.netease.course.service;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.course.dao.Contentdao;
import com.netease.course.dao.Trxdao;
import com.netease.course.dao.Userdao;
import com.netease.course.meta.Product;
import com.netease.course.meta.Trx;
import com.netease.course.meta.User;

@Service
public class userserviceimpl implements Userservice {
	@Autowired
	Userdao dao;
	@Autowired
	Contentdao contentdao;
	@Autowired
	Trxdao trxdao;
	
	public User find(String userName, String password) {
		return this.dao.find(userName, password);
	}
	public Product getproduct(int id) {
		Product product= contentdao.select(id);
		product.setPrice(product.getPrice()/100);
		try {
			product.setDetail(new String(product.getText(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(trxdao.selectid(product.getId())!=null){
			product.setIsBuy(true);
			product.setIsSell(true);
			product.setBuyPrice(trxdao.selectid(product.getId()).getPrice());
			product.setBuyTime(trxdao.selectid(product.getId()).getTime());
		}
		else{
			product.setIsBuy(false);
			product.setIsSell(false);
		}
		return product;
	};
	public void publiccontent(HttpServletRequest rq,boolean edit) throws SerialException, UnsupportedEncodingException, SQLException{
		String title=(String) rq.getParameter("title");
		String summary=(String)rq.getParameter("summary");
		String image=(String)rq.getParameter("image");
		Blob imageBlob = new SerialBlob(image.getBytes("UTF-8"));
		String detail=(String)rq.getParameter("detail");
		Blob detailBlob = new SerialBlob(detail.getBytes("UTF-8"));
		long price=(long) (100*(Float.parseFloat(rq.getParameter("price"))));
		if (price<0)
			return;	
		if(!edit)
		contentdao.insert(price, title, imageBlob, summary, detailBlob);
		else{
			int id=(Integer) rq.getAttribute("updateid");
			contentdao.update(price, title, imageBlob, summary, detailBlob,id);
		}
	}
	public Product getlastproduct(){
		Product product=contentdao.select(contentdao.selectlastId());
		return product;
	}
	public List<Product>getproductlist(){
		List<Product>productlist=contentdao.getproductlist();
		for(Product product:productlist){
			product.setPrice(product.getPrice()/100);
			if(trxdao.selectid(product.getId())!=null){
				product.setIsBuy(true);
				product.setIsSell(true);
				product.setBuyPrice(trxdao.selectid(product.getId()).getPrice());
				product.setBuyTime(trxdao.selectid(product.getId()).getTime());
			}
			else{
				product.setIsBuy(false);
				product.setIsSell(false);
			}
		}
		return productlist;
	};
	
	public void buy(Product product,int personId){
		trxdao.insert(product.getId(),personId,product.getBuyPrice(),System.currentTimeMillis());
	};
	public List<Product>getbuylist(int personId){
		List<Trx>trxlist=trxdao.selectpersonid(personId);
		List<Product>buylist = new ArrayList<Product>();
		for(Trx trx:trxlist){
			Product product=contentdao.select(trx.getContentId());
			product.setBuyTime(trx.getTime());
			product.setBuyPrice(trx.getPrice());
			buylist.add(product);
		}
		return buylist;
	}
	public void deleteproduct(int id){
		contentdao.delete(id);
	}
}
