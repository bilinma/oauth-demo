package com.hg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hg.dao.UserMapper;
import com.hg.domain.User;
import com.hg.service.UserService;

/**
 * 
 * @ClassName: BuserServiceImpl   
 * @Description: 用户管理
 * @author: WANG
 * @date 2017年4月11日 上午11:09:16
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User selectByPrimaryKey(Integer uid) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(uid);
		
	}

	@Override
	public String addString() {
		// TODO Auto-generated method stub
		return "userServiceAddString";
	}

	
}
