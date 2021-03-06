package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.PolicyAdspace;

public interface PolicyAdspaceDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    int insert(PolicyAdspace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    int insertSelective(PolicyAdspace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    PolicyAdspace selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    int updateByPrimaryKeySelective(PolicyAdspace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_adspace
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    int updateByPrimaryKey(PolicyAdspace record);
    
    int batchInsert(@Param("policyAdspaces") List<PolicyAdspace> policyAdspaces);

	int deleteByPolicyId(@Param("policyId") Integer policyId, @Param("adspaceIds") List<Integer> adspaceIds);
	
	/**
	 * 根据策略ID和媒体ID获取媒体关联的 DealId
	 * 
	 * @param policyId
	 * @param mediaId
	 * @return
	 */
	List<PolicyAdspace> selectByPolicyIdAndMediaId(@Param("policyId") Integer policyId, @Param("mediaId") Integer mediaId);
}