package com.netease.course.web.controller;


import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.course.meta.Product;
import com.netease.course.meta.User;
import com.netease.course.service.Userservice;

@Controller
public class controller {
	@Autowired
	Userservice userserviceimpl;
	
	@RequestMapping(value="/")
	public String index(HttpServletRequest rq, HttpServletResponse rp,Model map) {
		HttpSession session=rq.getSession();
		User user=(User) session.getAttribute("user");
		if(user!=null)
			map.addAttribute(user);
		List<Product>productList=userserviceimpl.getproductlist();
		map.addAttribute(productList);
		return "index";
		}
		
	@RequestMapping(value="/login")
	public String login() {		
		return "login";
		}
	@RequestMapping(value="api/login")
	public String apilogin(HttpServletRequest rq, HttpServletResponse rp,Model map) throws Exception {
		String userName;
		String userPassword;
		synchronized (this) {
			userName=rq.getParameter("userName");
			userPassword=rq.getParameter("password");
		}
		User user=this.userserviceimpl.find(userName, userPassword);
		if (user==null){
			map.addAttribute("code",401);
			map.addAttribute("message","登陆错误");
			map.addAttribute("result", false);
			return "";
		}
		else{
			HttpSession session=rq.getSession();
			session.setAttribute("user", user);
			map.addAttribute("code",200);
			map.addAttribute("message","");
			map.addAttribute("result", true);
			return "";
		}
	}
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest rq) {
		HttpSession session=rq.getSession();
		session.invalidate();
		return "login";
		}
	@RequestMapping(value="/show")
	public String show(HttpServletRequest rq,Model map,@RequestParam("id")int id) {		
		Product product=userserviceimpl.getproduct(id);
		map.addAttribute(product);
		return "show";
		}
	
	@RequestMapping(value="/public")
	public String _public(HttpServletRequest rq,Model map){
		return "public";
		}
	@RequestMapping(value="/publicSubmit")
	public String _publicsubmit(HttpServletRequest rq,Model map) throws SerialException, UnsupportedEncodingException, SQLException {
		userserviceimpl.publiccontent(rq,false);
		Product product=userserviceimpl.getlastproduct();
		if(product!=null)
		map.addAttribute(product);
		return "publicSubmit";
		}
	@RequestMapping(value="/edit")
	public String _edit(HttpServletRequest rq,Model map,@RequestParam("id")int id){
		Product product=userserviceimpl.getproduct(id);
		map.addAttribute(product);
		return "edit";
		}
	@RequestMapping(value="/editSubmit")
	public String _editSubmit(HttpServletRequest rq,Model map,@RequestParam("id")int id) throws SerialException, UnsupportedEncodingException, SQLException{
		rq.setAttribute("updateid", id);
		userserviceimpl.publiccontent(rq, true);
		Product product=userserviceimpl.getproduct(id);
		map.addAttribute(product);
		return "editSubmit";
		}
	@RequestMapping(value="api/buy")
	public String buy(HttpServletRequest rq,Model map){
		Product product=userserviceimpl.getproduct(Integer.parseInt(rq.getParameter("id")));
		product.setBuyPrice(product.getPrice());
		HttpSession session=rq.getSession();
		User user=(User) session.getAttribute("user");
		userserviceimpl.buy(product,user.getId());
		map.addAttribute("code",200);
		map.addAttribute("message","购买成功");
		map.addAttribute("result", true);
		return "";
	}
	@RequestMapping(value="/account")
	public String account(HttpServletRequest rq,Model map){
		HttpSession session=rq.getSession();
		User user=(User) session.getAttribute("user");
		List<Product>buyList=userserviceimpl.getbuylist(user.getId());
		map.addAttribute("buyList",buyList);
		return "account";
	}
	@RequestMapping(value="api/delete")
	public String delete(HttpServletRequest rq,Model map){
		userserviceimpl.deleteproduct(Integer.parseInt(rq.getParameter("id")));
		map.addAttribute("code",200);
		map.addAttribute("message","删除成功");
		map.addAttribute("result", true);
		return "";
	}
}
