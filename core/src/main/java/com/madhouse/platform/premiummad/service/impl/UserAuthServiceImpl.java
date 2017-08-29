package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.UserAuthDao;
import com.madhouse.platform.premiummad.entity.UserAuth;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserAuthServiceImpl implements IUserAuthService {
	
	@Autowired
	private UserAuthDao userAuthDao;

	@Override
	public List<Integer> queryMediaIdList(Integer userId, String mediaIds) {
		int count = userAuthDao.checkAdminForMedia(userId);
		String[] idStrs = StringUtils.splitToStringArray(mediaIds);
		return userAuthDao.queryMediaIdList(userId, idStrs, count);
	}

	@Override
	public List<Integer> queryAdspaceIdList(Integer userId, String adspaceIds) {
		int count = userAuthDao.checkAdminForMedia(userId);
		String[] idStrs = StringUtils.splitToStringArray(adspaceIds);
		return userAuthDao.queryAdspaceIdList(userId, idStrs, count);
	}
	
	@Override
	public List<Integer> queryPolicyIdList(Integer userId, String policyIds) {
		//根据权限表里有无policy_id为-1的数据来判断此用户是否Admin
		int count = userAuthDao.checkAdminForPolicy(userId);
		String[] idStrs = StringUtils.splitToStringArray(policyIds);
		//通过Admin的标志来做全搜索或是过滤
		return userAuthDao.queryPolicyIdList(userId, idStrs, count);
	}

	@Override
	public void updateUserMediaAuth(UserAuth userAuth) {
		Integer isAdmin = userAuth.getIsAdmin();
		if(isAdmin != null && isAdmin.intValue() == 1){ //设置成管理员权限
			Integer[] mediaIdsForAdmin = new Integer[1];
			mediaIdsForAdmin[0] = -1;
			userAuth.setMediaIds(mediaIdsForAdmin);
		}
		userAuthDao.removeUserMediaAuth(userAuth.getUserId());
		
		Integer[] ids = userAuth.getMediaIds();
		if(ids != null && ids.length > 0){
			userAuthDao.addUserMediaAuth(userAuth);
		}
	}
	
	@Override
	public void updateUserPolicyAuth(UserAuth userAuth) {
		Integer isAdmin = userAuth.getIsAdmin();
		if(isAdmin != null && isAdmin.intValue() == 1){ //设置成管理员权限
			Integer[] idsForAdmin = new Integer[1];
			idsForAdmin[0] = -1;
			userAuth.setPolicyIds(idsForAdmin);
		}
		userAuthDao.removeUserPolicyAuth(userAuth.getUserId());
		
		Integer[] ids = userAuth.getPolicyIds();
		if(ids != null && ids.length > 0){
			userAuthDao.addUserPolicyAuth(userAuth);
		}
	}

}
