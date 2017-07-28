package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.MediaMetaData;

public interface IMediaService {
	/**
     * 查询所有供应方媒体
     * @return List
     */
    List<MediaMetaData> queryAll();

    
}
