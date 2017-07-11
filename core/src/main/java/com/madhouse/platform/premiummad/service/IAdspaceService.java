package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;

public interface IAdspaceService {
	/**
     * 查询所有广告位
     * @return List
     */
    List<Adspace> queryAll(String ids);

    /**
     * 新建广告位
     * @param Adspace 广告位对象
     */
    Integer insert(Adspace adspace);

    /**
     * 根据广告位Id查询
     * @param AdspaceId 广告位Id
     * @return Adspace
     */
    Adspace queryAdspaceById(Integer adspaceId);

    /**
     * 更新广告位
     * @param adspace 广告位对象
     */
    Integer update(Adspace adspace);

    /**
     * 检查广告位名称
     * @param AdspaceName 广告位名称
     * @return Integer 返回按名称条件查询的数据条数
     */
    Integer checkName(String adspaceName);

    /**
     * 仅更新广告位状态
     * @param adspace
     * @return
     */
	Integer updateStatus(Adspace adspace);

	/**
	 * 添加我方广告位和媒体方广告位的映射关系
	 * @param adspaceMapping
	 */
	StatusCode addAdspaceMediaMapping(AdspaceMapping adspaceMapping);

	/**
	 * 添加我方广告位和（可能多方）dsp方信息的映射关系
	 * @param dspMappings
	 * @return
	 */
	StatusCode addAdspaceDspMapping(List<DspMapping> dspMappings);
}
