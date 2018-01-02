package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.dao.DspMediaMapper;
import com.madhouse.platform.premiummad.dao.MimesDao;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.AdspaceUnion;
import com.madhouse.platform.premiummad.entity.AuditedAdspaceQueryParam;
import com.madhouse.platform.premiummad.entity.DspMapping;
import com.madhouse.platform.premiummad.entity.DspMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.AdspaceModel;
import com.madhouse.platform.premiummad.rule.AdspaceRule;
import com.madhouse.platform.premiummad.rule.DspMediaRule;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdspaceServiceImpl implements IAdspaceService {

	@Autowired
	private AdspaceDao adspaceDao;

	@Autowired
	private MimesDao mimesDao;
	
	@Autowired
	private DspMediaMapper dspMediaDao;

	@Override
	public List<Adspace> queryAllByParams(List<Integer> mediaIdList, Integer status, Integer mediaCategory) {
		return adspaceDao.queryAllByParams(mediaIdList, status, mediaCategory);
	}

	@Override
	public Integer insert(Adspace adspace, String xFrom) {
		Integer count = checkName(adspace.getName().trim());
		if (count > 0) // 检查名称
			throw new BusinessException(StatusCode.SC20207);
		adspaceDao.insert(adspace);

		postprocessAdspaceParams(adspace, xFrom);
		return updateAdspaceKey(adspace);
	}

	private void postprocessAdspaceParams(Adspace adspace, String xFrom) {
		// 生成adspaceKey，更新入到数据库e
		Integer id = adspace.getId();
		String name = adspace.getName();
		if (StringUtils.isEmpty(xFrom)) { // 防止前端请求过来没有xfrom请求头参数
			xFrom = SystemConstant.Request.XFROM_DEFAULT_VALUE;
		}
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
	public Adspace queryAdspacePolicies(Integer adspaceId) {
		return adspaceDao.queryAdspacePolicies(adspaceId);
	}

	@Override
	public int update(Adspace adspace) {
		Adspace queryResult = queryById(adspace.getId());
		if (queryResult == null)
			throw new BusinessException(StatusCode.SC20003);
		if (!queryResult.getName().equals(adspace.getName())) { // 名称不相等,检查名称
			Integer count = checkName(adspace.getName().trim());
			if (count > 0)
				throw new BusinessException(StatusCode.SC20207);
		}

		return adspaceDao.update(adspace);
	}

	public Integer updateAdspaceKey(Adspace adspace) {
		int queryResult = adspaceDao.queryByAdspaceKey(adspace.getAdspaceKey());
		if (queryResult > 0) {
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
		if (!StringUtils.isEmpty(mediaAdspaceKey)) { // 媒体映射信息存在
			int queryResult = queryAdspaceMediaMapping(adspaceMapping);
			if (queryResult > 0) { // 我方广告位ID和媒体方广告位Key皆不可重复
				return StatusCode.SC20201;
			}

			adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		}

		return StatusCode.SC20000;
	}

	private int queryAdspaceMediaMapping(AdspaceMapping queryParam) {
		return adspaceDao.queryAdspaceMediaMapping(queryParam);
	}

	// 判断一次设入的dspId是否有重复
	private void checkDspMapping(List<DspMapping> dspMappings) {
		Set<Integer> dspIdSet = new HashSet<Integer>();
		boolean result = true;
		for (DspMapping dspMapping : dspMappings) {
			Integer dspId = dspMapping.getDspId();
			String dspMediaId = dspMapping.getDspMediaId();
			String dspAdspaceKey = dspMapping.getDspAdspaceKey();
			result = dspIdSet.add(dspId);
			if (!result) { // dspId重复
				throw new BusinessException(StatusCode.SC20202);
			}

			if (!StringUtils.isEmpty(dspId)) {
				if (StringUtils.isEmpty(dspMediaId) && StringUtils.isEmpty(dspAdspaceKey)) {// DSP广告位的映射信息不全
					throw new BusinessException(StatusCode.SC20203);
				}
			}

			// dsp媒体和广告位信息有值，那么dspId比填
			if (!(StringUtils.isEmpty(dspMediaId) && StringUtils.isEmpty(dspAdspaceKey))) {
				if (StringUtils.isEmpty(dspId)) {
					throw new BusinessException(StatusCode.SC20204);
				}
			}
		}
	}

	@Override
	public AdspaceMapping queryAdspaceMappingById(Integer id) {
		List<AdspaceMapping> queryObjects = adspaceDao.queryAdspaceMappingById(id);
		if (queryObjects != null && queryObjects.size() > 0) {
			return queryObjects.get(0);
		}
		return null;
	}

	@Override
	public int createAndUpdateAdspaceMapping(AdspaceMapping adspaceMapping) {
		List<AdspaceMapping> queryObjects = adspaceDao.queryAdspaceMappingById(adspaceMapping.getAdspaceId());
		if (queryObjects != null && queryObjects.size() > 0) { // 数据库里有映射信息，先删除
			removeAdspaceMapping(adspaceMapping.getAdspaceId());
		}

		// 插入更新数据前做check
		String mediaAdspaceKey = adspaceMapping.getMediaAdspaceKey();
		if (!StringUtils.isEmpty(mediaAdspaceKey)) { // 媒体映射信息存在
			AdspaceMapping queryParam = new AdspaceMapping();
			queryParam.setMediaAdspaceKey(mediaAdspaceKey);
			int queryResult = queryAdspaceMediaMapping(queryParam);
			if (queryResult > 0) { // 媒体方广告位Key不可重复
				throw new BusinessException(StatusCode.SC20201);
			}
			adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		}

		List<DspMapping> dspMappings = adspaceMapping.getDspMappings();
		if (dspMappings != null && dspMappings.size() > 0) {
			checkDspMapping(dspMappings);
			adspaceDao.insertAdspaceDspMapping(dspMappings);
		}

		return 0;
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
	public List<Adspace> queryAll(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取已启用的广告位，并封装
	 * 
	 * @param dspId
	 */
	@Override
	public List<AdspaceModel> getAuditedAdspaces(String dspId) {
		List<AdspaceModel> adspaces = new ArrayList<AdspaceModel>();

		// 获取该dsp下有权限的广告位和媒体
		List<DspMedia> dspMedias = dspMediaDao.selectByDspId(dspId);
		if (dspMedias == null || dspMedias.isEmpty()) {
			return adspaces;
		}
		
		// 构造查询请求参数
		AuditedAdspaceQueryParam queryParam = new AuditedAdspaceQueryParam();
		queryParam.setDspId(dspId);
		DspMediaRule.splitMeidaAndAds(queryParam, dspMedias);

		// 获取已启用的广告位
		List<AdspaceUnion> auditedAdspaces = adspaceDao.selectAuditedAdspaces(queryParam);
		if (auditedAdspaces == null || auditedAdspaces.isEmpty()) {
			return adspaces;
		}

		// 封装返回对象
		for (AdspaceUnion adspace : auditedAdspaces) {
			List<String> meterialMinesTypes = mimesDao.queryMimesById(AdspaceRule.getTypes(adspace.getMaterialType()));
			List<String> coverMinesTypes = mimesDao.queryMimesById(AdspaceRule.getTypes(adspace.getCoverType()));
			List<String> logoMinesTypes = mimesDao.queryMimesById(AdspaceRule.getTypes(adspace.getLogoType()));
//			SysMedia media = mediaDao.selectByPrimaryKey(adspace.getMediaId());
//			AdspaceMappingDsp adspaceMappingDsp = adspaceMappingDspDao.selectByAdspaceIdAndDspId(adspace.getId(), Integer.valueOf(dspId));
			AdspaceModel adspaceModel = AdspaceRule.buildAdspace(adspace, meterialMinesTypes, coverMinesTypes, logoMinesTypes/*, media, adspaceMappingDsp*/);
			adspaces.add(adspaceModel);
		}

		return adspaces;
	}
}
