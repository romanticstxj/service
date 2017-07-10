package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.UserAuthDao;
import com.madhouse.platform.premiummad.service.IUserAuthService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserAuthServiceImpl implements IUserAuthService {
	
	@Autowired
	private UserAuthDao userAuthDao;

	@Override
	public List<Integer> queryMediaIdListByUserId(Integer userId) {
		
		return userAuthDao.queryMediaIdListByUserId(userId == null? 0: userId);
	}

}
