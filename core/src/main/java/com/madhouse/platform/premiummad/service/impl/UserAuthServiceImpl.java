package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.UserAuthDao;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserAuthServiceImpl implements IUserAuthService {
	
	@Autowired
	private UserAuthDao userAuthDao;

	@Override
	public List<Integer> queryMediaIdList(Integer userId, String mediaIds) {
		String[] idStrs = StringUtils.splitToStringArray(mediaIds);
		return userAuthDao.queryMediaIdList(userId, idStrs);
	}

	@Override
	public List<Integer> queryAdspaceIdList(Integer userId, String adspaceIds) {
		String[] idStrs = StringUtils.splitToStringArray(adspaceIds);
		return userAuthDao.queryAdspaceIdList(userId, idStrs);
	}
	
	@Override
	public List<Integer> queryPolicyIdList(Integer userId, String policyIds) {
		String[] idStrs = StringUtils.splitToStringArray(policyIds);
		return userAuthDao.queryPolicyIdList(userId, idStrs);
	}

}
