package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.ReqBlockDao;
import com.madhouse.platform.premiummad.entity.ReqBlock;
import com.madhouse.platform.premiummad.service.IReqBlockService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ReqBlockServiceImpl implements IReqBlockService {
	
	@Autowired
	private ReqBlockDao blockDao ;

    @Override
    public List<ReqBlock> queryAll() {
        return blockDao.queryAll();
    }
	
}
