package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.MediaMappingMetaData;

public interface IPlcmtService {
    
    
    List<Adspace> queryAll();

    List<String> queryMimesById(List<Integer> list);
    
    List<MediaMappingMetaData> queryAdspaceMappingMedia();
}
