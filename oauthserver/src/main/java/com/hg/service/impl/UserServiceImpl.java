package com.hg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hg.dao.UserMapper;
import com.hg.domain.User;
import com.hg.service.UserService;

/**
 * 
 * @author xiaobin.ma
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User selectByPrimaryKey(Integer uid) {
		return userMapper.selectByPrimaryKey(uid);
		
	}

	@Override
	public String addString() {
		return "userServiceAddString";
	}

	
}
