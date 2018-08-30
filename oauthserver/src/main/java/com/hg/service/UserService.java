package com.hg.service;

import com.hg.domain.User;

public interface UserService {

	User selectByPrimaryKey(Integer uid);
	
	String addString();
}
