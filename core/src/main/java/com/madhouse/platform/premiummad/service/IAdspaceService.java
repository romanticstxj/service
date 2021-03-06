package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.model.AdspaceModel;

public interface IAdspaceService extends IBaseService<Adspace>{
	/**
     * 查询所有广告位
     * @return List
     */
    List<Adspace> queryAllByParams(List<Integer> mediaIdList, Integer status, Integer mediaCategory);
    
    Adspace queryAdspacePolicies(Integer id);

    /**
     * 新建广告位
     * @param AdspaceDspModel 广告位对象
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

	/**
	 * 获取已启用的广告位
	 * 
	 * @param dspId
	 * @return
	 */
	List<AdspaceModel> getAuditedAdspaces(String dspId);
}
