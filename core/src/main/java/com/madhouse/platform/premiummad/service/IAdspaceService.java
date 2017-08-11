package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;

public interface IAdspaceService extends IBaseService<Adspace>{
	/**
     * 查询所有广告位
     * @return List
     */
    List<Adspace> queryAllByParams(List<Integer> mediaIdList, Integer status);

    /**
     * 新建广告位
     * @param Adspace 广告位对象
     */
    Integer insert(Adspace adspace, String xFrom);

	/**
	 * 添加我方广告位和媒体方广告位的映射关系
	 * @param adspaceMapping
	 */
	StatusCode addAdspaceMediaMapping(AdspaceMapping adspaceMapping);


	AdspaceMapping queryAdspaceMappingById(Integer id);

	int createAndUpdateAdspaceMapping(AdspaceMapping adspaceMapping);

	int removeAdspaceMapping(Integer adspaceId);

}
