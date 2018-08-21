package com.hg.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hg.domain.User;



public interface UserService {

	User selectByPrimaryKey(Integer uid);
	
	String addString();
}
