package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Media;

public interface IMediaService {
	/**
     * 查询所有供应方媒体
     * @return List
     */
    List<Media> queryAll(String ids);

    /**
     * 新建供应方媒体
     * @param Media 供应方媒体对象
     */
    Integer insert(Media media);

    /**
     * 根据供应方媒体Id查询
     * @param MediaId 供应方媒体Id
     * @return Media
     */
    Media queryMediaById(Integer mediaId);

    /**
     * 更新媒体
     * @param media 媒体对象
     */
    Integer update(Media media);

    /**
     * 检查供应方媒体名称
     * @param MediaName 供应方媒体名称
     * @return Integer 返回按名称条件查询的数据条数
     */
    Integer checkName(String mediaName);

    /**
     * 仅更新媒体状态
     * @param media
     * @return
     */
	Integer updateStatus(Media media);
}
