package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.DSPDao;
import com.madhouse.platform.premiummad.entity.DSPMappingMetaData;
import com.madhouse.platform.premiummad.entity.DSPMetaData;
import com.madhouse.platform.premiummad.service.IDSPService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DSPServiceImpl implements IDSPService {
	
	@Autowired
	private DSPDao dspDao;

    @Override
    public List<DSPMetaData> queryAll() {
        return dspDao.queryAll();
    }

    @Override
    public List<DSPMappingMetaData> queryAdspaceMappingDsp() {
        return dspDao.queryAdspaceMappingDsp();
    }

}
