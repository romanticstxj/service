package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.MaterialMedia;
import com.madhouse.platform.premiummad.entity.MaterialMediaUnion;
import com.madhouse.platform.premiummad.model.MaterialMediaModel;

public interface MaterialMediaMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	int insert(MaterialMedia record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	int insertSelective(MaterialMedia record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	MaterialMedia selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	int updateByPrimaryKeySelective(MaterialMedia record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material_media
	 * @mbggenerated  Mon Jul 10 11:12:20 CST 2017
	 */
	int updateByPrimaryKey(MaterialMedia record);

	/**
	 * 根据DSP定义的素材ID获取审核结果
	 * @param materialKeys
	 * @return
	 */
	List<MaterialMediaUnion> selectMaterialMedias(String[] materialKeys);
	
	/**
	 * 根据DSP定义的素材ID和媒体ID列表查询素材和媒体提交的记录
	 * @param param
	 * @return
	 */
	List<MaterialMediaUnion> selectByMaterialKeyAndMediaIds(MaterialMediaModel param);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertByBatch(List<MaterialMedia> list);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateByBath(List<MaterialMedia> list);
	
	/**
	 * 审核时更新相关信�?
	 * @param record
	 * @return
	 */
	int updateForAudit(MaterialMedia record);
}