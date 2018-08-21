package com.hg.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hg.domain.User;
import com.hg.service.UserService;


/**
 * 
 * @ClassName: LoginController   
 * @Description: 登录controller
 * @Description:跳转到登录界面
 */

@RequestMapping("/user")
@Controller
public class UserController{
	
	private static Logger logger = Logger.getLogger(UserInfoController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/showUser")
	public ModelAndView toShowUser(){
		User user = userService.selectByPrimaryKey(1);
		String string = userService.addString();
		ModelAndView mv = new ModelAndView("user01");
		mv.addObject("user", user);
		mv.addObject("ok", "ok01");
		mv.addObject("string", string);
		logger.debug(user.getUid());
		logger.debug(user.getUname());
		logger.debug(user.getUpassword());
		logger.debug(user.getUemail());
		return mv;
	}
	
	
	
}