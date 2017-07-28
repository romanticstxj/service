package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Dsp;

public interface DspMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    int insert(Dsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    int insertSelective(Dsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    Dsp selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    int updateByPrimaryKeySelective(Dsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_dsp
     *
     * @mbggenerated Wed Jul 05 16:45:10 CST 2017
     */
    int updateByPrimaryKey(Dsp record);
    
    /**
     * 根据 id 和 token 查询
     * @param record
     * @return
     */
    Dsp selectByIdAndToken(Dsp record);
    
    /**
     * 检查名字重复
     * @param dspName
     * @return
     */
    int checkName(String dspName);

    /**
     * 更新状态
     * @param dsp
     * @return
     */
	int updateStatus(Dsp dsp);

	/**
	 * 查询dsp列表，可以含参ids
	 * @param idStrs
	 * @return
	 */
	List<Dsp> queryAll(@Param("idStrs") String[] idStrs);
}