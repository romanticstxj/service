package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Policy;

public interface PolicyDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    int insert(Policy record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    int insertSelective(Policy record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    Policy selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    int updateByPrimaryKeySelective(Policy record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy
     *
     * @mbggenerated Wed Jul 19 11:37:35 CST 2017
     */
    int updateByPrimaryKey(Policy record);
    
    int checkName(String name);
    
    Policy selectCascadedlyByPrimaryKey(Integer id);

	int update(Policy policy);
	
	List<Policy> queryAllByParams(@Param("idStrs") List<Integer> idStrs, @Param("status") Integer status, 
			@Param("type") Integer type);

	int updateStatus(Policy policy);
	
	Policy selectPolicy(Integer id);
}