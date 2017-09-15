package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.model.MaterialModel;

public interface MaterialMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	int insert(Material record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	int insertSelective(Material record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	Material selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	int updateByPrimaryKeySelective(Material record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table mad_sys_material
	 * @mbggenerated  Fri Sep 01 11:39:46 CST 2017
	 */
	int updateByPrimaryKey(Material record);

	/**
	 * 根据素材key和媒体ID查询
	 * 
	 * @param param
	 * @return
	 */
	List<Material> selectByMaterialKeyAndMediaIds(MaterialModel param);

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 */
	int insertByBatch(List<Material> list);

	/**
	 * 批量更新
	 * 
	 * @param list
	 * @return
	 */
	int updateByBath(List<Material> list);

	/**
	 * 插入素材信息，返回主键
	 * 
	 * @param record
	 * @return
	 */
	int insertMaterial(Material record);

	/**
	 * 根据DSP提供的素材IDs和dspID查询
	 * 
	 * @param materialKeys
	 * @param dspId
	 * @return
	 */
	List<Material> selectByMaterialKeysAndDspId(@Param("materialKeys") String[] advertiserKeys, @Param("dspId") String dspId);

	/**
	 * 根据主键ID获取素材
	 * 
	 * @param list
	 * @return
	 */
	List<Material> selectByIds(List<Integer> list);
	
	/**
	 * 获取需要审核媒体的广告主
	 * 
	 * @param mediaId
	 * @return
	 */
	List<Material> selectMediaMaterials(@Param("mediaId")Integer mediaId, @Param("auditStatus")Integer auditStatus);
	
	/**
	 * 根据媒体id和媒体方素材key更新
	 * @param record
	 * @return
	 */
	int updateByMediaAndMediaQueryKey(Material record);
	
	/**
	 * 查询所有素材
	 * 
	 * @param list
	 * @return
	 */
	List<Material> queryAll(@Param("mediaIds") List<Integer> mediaIds);

	/**
	 * 查询某个特定素材
	 * @return
	 */
	Material queryById(Integer id);

	/**
	 * 根据媒体key和媒体素材key查询
	 * 
	 * @param mediaQueryKeys
	 * @param mediaId
	 * @return
	 */
	List<Material> selectMaterials(@Param("mediaQueryKeys") String[] mediaQueryKeys, @Param("mediaId")Integer mediaId);

	void auditMaterial(@Param("ids") String[] ids, @Param("status") Integer status, 
			@Param("reason") String reason, @Param("userId") Integer userId);
	
	int judgeWhetherCanAudit(@Param("ids") String[] ids);
	
	List<String> selectAuditableMaterials(@Param("ids") String[] ids);
}
