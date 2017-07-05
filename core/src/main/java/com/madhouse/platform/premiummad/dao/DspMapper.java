package com.madhouse.platform.premiummad.dao;

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
}