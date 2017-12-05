package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.RequestBlockDao;
import com.madhouse.platform.premiummad.entity.RequestBlock;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IRequestBlockService;

@Service
@Transactional(rollbackFor=RuntimeException.class)
public class RequestBlockServiceImpl implements IRequestBlockService {
	
	@Autowired
	private RequestBlockDao requestBlockDao;

	@Override
	public int insert(RequestBlock t) {
		int count = this.checkName(t.getCode());
		if(count > 0){ //异常流量重名
			throw new BusinessException(StatusCode.SC20601);
		}
		 
		return requestBlockDao.insertSelective(t);
	}
	
	@Override
	public int updateStatus(RequestBlock t) {
		RequestBlock queryResult = requestBlockDao.selectByPrimaryKey(t.getId());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20003);
        return requestBlockDao.updateStatus(t);
	}
	
	@Override
	public int checkName(String code) {
		int count = requestBlockDao.checkName(code);
		return count;
	}

	@Override
	public List<RequestBlock> list(Integer type) {
		List<RequestBlock> list = requestBlockDao.queryAllByParams(type);
		return list;
	}

	@Override
	public int update(RequestBlock t) {
		return 0;
	}

	@Override
	public RequestBlock queryById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RequestBlock> queryAll(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
