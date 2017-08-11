package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MaterialDao;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MaterialServiceImpl implements IMaterialService {
    
    @Autowired
    private MaterialDao dao;
    
    @Override
    public List<Material> queryAll() {
        return dao.queryAll();
    }
    
}
