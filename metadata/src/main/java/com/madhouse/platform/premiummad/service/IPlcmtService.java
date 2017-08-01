package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Adspace;

public interface IPlcmtService {
    
    
    List<Adspace> queryAll();

    List<String> queryMimesById(List<Integer> list);
}
