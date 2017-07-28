package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.model.AdvertiserModel;

public interface AdvertiserMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	int insert(Advertiser record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	int insertSelective(Advertiser record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	Advertiser selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	int updateByPrimaryKeySelective(Advertiser record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_advertiser
	 * @mbggenerated  Wed Jul 26 10:30:28 CST 2017
	 */
	int updateByPrimaryKey(Advertiser record);

	/**
	 * 根据广告主ID和媒体ID查询
	 * 
	 * @param param
	 * @return
	 */
	List<Advertiser> selectByAdvertiserKeyAndMediaIds(AdvertiserModel param);

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 */
	int insertByBatch(List<Advertiser> list);

	/**
	 * 批量更新
	 * 
	 * @param list
	 * @return
	 */
	int updateByBath(List<Advertiser> list);

	/**
	 * 新增 返回主键
	 * 
	 * @param record
	 * @return
	 */
	int insertAdvertiser(Advertiser record);

	/**
	 * 根据DSP提供的广告主IDs和dspID查询
	 * 
	 * @param advertiserKeys
	 * @param dspId
	 * @param mediaId
	 * @return
	 */
	List<Advertiser> selectByAdvertiserKeysAndDspId(@Param("advertiserKeys") String[] advertiserKeys, @Param("dspId") String dspId, @Param("mediaId") Integer mediaId);

	/**
	 * 根据主键ID获取广告主
	 * 
	 * @param list
	 * @return
	 */
	List<Advertiser> selectByIds(List<Integer> list);
	
	/**
	 * 获取需要审核媒体的广告主
	 * 
	 * @param mediaId
	 * @return
	 */
	List<Advertiser> selectMediaAdvertisers(@Param("mediaId")Integer mediaId, @Param("auditStatus")Integer auditStatus);
}