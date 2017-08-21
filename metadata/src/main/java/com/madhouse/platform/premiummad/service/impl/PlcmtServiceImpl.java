package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MimesDao;
import com.madhouse.platform.premiummad.dao.PlcmtDao;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.MediaMappingMetaData;
import com.madhouse.platform.premiummad.service.IPlcmtService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PlcmtServiceImpl implements IPlcmtService {
	
	@Autowired
	private PlcmtDao plcmtDao;
	
	@Autowired
    private MimesDao mimesDao;

    @Override
    public List<Adspace> queryAll() {
        return plcmtDao.queryAll();
    }

    @Override
    public List<String> queryMimesById(List<Integer> list) {
        return mimesDao.queryMimesById(list);
    }

    @Override
    public List<MediaMappingMetaData> queryAdspaceMappingMedia() {
        return plcmtDao.queryMediaMappingMetaData();
    }

	
}
