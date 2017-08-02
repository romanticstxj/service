package com.madhouse.platform.premiummad.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdspaceServiceImpl implements IAdspaceService {
	
	@Autowired
	private AdspaceDao adspaceDao;
	
	@Override
	public List<Adspace> queryAllByParams(String ids, Integer status) {
		String[] idStrs = StringUtils.splitToStringArray(ids);
		return adspaceDao.queryAllByParams(idStrs, status);
	}

	@Override
	public Integer insert(Adspace adspace, String xFrom) {
		Integer count = checkName(adspace.getName().trim());
        if (count > 0) //检查名称
        	throw new BusinessException(StatusCode.SC20207);
		adspaceDao.insert(adspace);
        
        postprocessAdspaceParams(adspace, xFrom);
        return updateAdspaceKey(adspace);
	}
	
	private void postprocessAdspaceParams(Adspace adspace, String xFrom) {
		//生成adspaceKey，更新入到数据库e
		Integer id = adspace.getId();
		String name = adspace.getName();
		String combinedStr = new StringBuffer(id.toString()).append(name).append(xFrom).toString();
		String adspaceKey = StringUtils.getMD5(combinedStr);
		String truncatedAdspaceKey = adspaceKey.substring(8, 24);
		adspace.setAdspaceKey(truncatedAdspaceKey);
	}

	@Override
	public Adspace queryById(Integer adspaceId) {
		return adspaceDao.queryAdspaceById(adspaceId);
	}

	@Override
	public int update(Adspace adspace) {
		Adspace queryResult = queryById(adspace.getId());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20002);
        if (!queryResult.getName().equals(adspace.getName())) { //名称不相等,检查名称
            Integer count = checkName(adspace.getName().trim());
            if (count > 0)
            	throw new BusinessException(StatusCode.SC20207);
        }
        
		return adspaceDao.update(adspace);
	}
	
	public Integer updateAdspaceKey(Adspace adspace) {
		int queryResult = adspaceDao.queryByAdspaceKey(adspace.getAdspaceKey());
		if(queryResult > 0){
			throw new BusinessException(StatusCode.SC20206);
		}
		BeanUtils.setUpdateParam(adspace);
		return adspaceDao.updateAdspaceKey(adspace);
	}

	@Override
	public int checkName(String adspaceName) {
		return adspaceDao.checkName(adspaceName);
	}

	@Override
	public int updateStatus(Adspace adspace) {
		return adspaceDao.updateStatus(adspace);
	}

	@Override
	public StatusCode addAdspaceMediaMapping(AdspaceMapping adspaceMapping) {
		String mediaAdspaceKey = adspaceMapping.getMediaAdspaceKey();
		if(!StringUtils.isEmpty(mediaAdspaceKey)){ //媒体映射信息存在
			int queryResult = queryAdspaceMediaMapping(adspaceMapping);
			if(queryResult > 0){ //我方广告位ID和媒体方广告位Key皆不可重复
				return StatusCode.SC20201;
			}
			
			adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		}
		
		return StatusCode.SC20000;
	}

	private int queryAdspaceMediaMapping(AdspaceMapping queryParam) {
		return adspaceDao.queryAdspaceMediaMapping(queryParam);
	}


	//插入dsp映射关系表时查询有没有已经存在的我方广告位Id
	private int queryAdspaceDspMapping(Integer adspaceId) {
		return adspaceDao.queryAdspaceDspMapping(adspaceId);
	}

	//判断一次设入的dspId是否有重复
	private boolean isDspMappingDuplicated(List<DspMapping> dspMappings) {
		Set<Integer> dspIdSet = new HashSet<Integer>();
		boolean result = true;
		for(DspMapping dspMapping : dspMappings){
			Integer dspId = dspMapping.getDspId();
			result = dspIdSet.add(dspId);
			if(!result) break;
		}
		return result;
	}

	@Override
	public AdspaceMapping queryAdspaceMappingById(Integer id) {
		return adspaceDao.queryAdspaceMappingById(id);
	}

	@Override
	public StatusCode addAdspaceMapping(AdspaceMapping adspaceMapping) {
		Integer adspaceId = adspaceMapping.getAdspaceId();
		String mediaAdspaceKey = adspaceMapping.getMediaAdspaceKey();
		if(adspaceId != null){ 
			AdspaceMapping queryParam = new AdspaceMapping();
			queryParam.setAdspaceId(adspaceId);
			int queryResult = queryAdspaceMediaMapping(queryParam);
			if(queryResult > 0){ //我方广告位ID不可重复
				return StatusCode.SC20203;
			}
		}
		if(!StringUtils.isEmpty(mediaAdspaceKey)){ //媒体映射信息存在
			AdspaceMapping queryParam = new AdspaceMapping();
			queryParam.setMediaAdspaceKey(mediaAdspaceKey);
			int queryResult = queryAdspaceMediaMapping(queryParam);
			if(queryResult > 0){ //媒体方广告位Key不可重复
				return StatusCode.SC20201;
			}
		}
		
		List<DspMapping> dspMappings = adspaceMapping.getDspMappings();
		if(dspMappings != null && dspMappings.size() > 0){
			int queryResult = queryAdspaceDspMapping(adspaceId);
			if(queryResult > 0){ //我方广告位ID不可重复
				return StatusCode.SC20203;
			}
			
			boolean result = isDspMappingDuplicated(dspMappings);
			if(!result){ //DSP ID不可重复
				return StatusCode.SC20202;
			}
		}
		
		adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		adspaceDao.insertAdspaceDspMapping(dspMappings);
		
		return StatusCode.SC20000;
	}

	@Override
	public StatusCode updateAdspaceMapping(AdspaceMapping adspaceMapping) {
		AdspaceMapping queryObject = queryAdspaceMappingById(adspaceMapping.getAdspaceId());
		if(queryObject == null){ //需要更新的广告位id不存在映射信息表中
			return StatusCode.SC20205;
		}
		
		adspaceDao.removeAdspaceMediaMapping(adspaceMapping.getAdspaceId());
		adspaceDao.removeAdspaceDspMapping(adspaceMapping.getAdspaceId());
		
		String mediaAdspaceKey = adspaceMapping.getMediaAdspaceKey();
		if(!StringUtils.isEmpty(mediaAdspaceKey)){ //媒体映射信息存在
			AdspaceMapping queryParam = new AdspaceMapping();
			queryParam.setMediaAdspaceKey(mediaAdspaceKey);
			int queryResult = queryAdspaceMediaMapping(queryParam);
			if(queryResult > 0){ //媒体方广告位Key不可重复
				return StatusCode.SC20201;
			}
		}
		
		List<DspMapping> dspMappings = adspaceMapping.getDspMappings();
		if(dspMappings != null && dspMappings.size() > 0){
			boolean result = isDspMappingDuplicated(dspMappings);
			if(!result){ //DSP ID不可重复
				return StatusCode.SC20202;
			}
		}
		
		
		adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		adspaceDao.insertAdspaceDspMapping(dspMappings);
		
		return StatusCode.SC20000;
	}
	
	@Override
	public int createAndUpdateAdspaceMapping(AdspaceMapping adspaceMapping) {
		AdspaceMapping queryObject = queryAdspaceMappingById(adspaceMapping.getAdspaceId());
		if(queryObject != null){ //数据库里有映射信息，先删除
			removeAdspaceMapping(adspaceMapping.getAdspaceId());
		}
		
		//插入更新数据前做check
		String mediaAdspaceKey = adspaceMapping.getMediaAdspaceKey();
		if(!StringUtils.isEmpty(mediaAdspaceKey)){ //媒体映射信息存在
			AdspaceMapping queryParam = new AdspaceMapping();
			queryParam.setMediaAdspaceKey(mediaAdspaceKey);
			int queryResult = queryAdspaceMediaMapping(queryParam);
			if(queryResult > 0){ //媒体方广告位Key不可重复
				throw new BusinessException(StatusCode.SC20201);
			}
		}
		
		List<DspMapping> dspMappings = adspaceMapping.getDspMappings();
		if(dspMappings != null && dspMappings.size() > 0){
			boolean result = isDspMappingDuplicated(dspMappings);
			if(!result){ //DSP ID不可重复
				throw new BusinessException(StatusCode.SC20202);
			}
		}
		
		if(dspMappings != null && dspMappings.size()>0){
			adspaceDao.insertAdspaceDspMapping(dspMappings);
		}
		return adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
	}

	public int removeAdspaceMapping(Integer adspaceId) {
		adspaceDao.removeAdspaceMediaMapping(adspaceId);
		return adspaceDao.removeAdspaceDspMapping(adspaceId);
	}

	@Override
	public int insert(Adspace t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Adspace> queryAll(String ids) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
