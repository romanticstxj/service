package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.DSPMetaData;

public interface IDSPService {
	/**
     * 查询所有dsp
     * @return List
     */
    List<DSPMetaData> queryAll();

    
}
