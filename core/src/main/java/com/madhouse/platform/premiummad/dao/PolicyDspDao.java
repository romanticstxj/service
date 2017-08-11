package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.PolicyDsp;

public interface PolicyDspDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    int insert(PolicyDsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    int insertSelective(PolicyDsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    PolicyDsp selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    int updateByPrimaryKeySelective(PolicyDsp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mad_sys_policy_dsp
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    int updateByPrimaryKey(PolicyDsp record);

	int batchInsert(@Param("policyDsps") List<PolicyDsp> policyDsps);

	int deleteByPolicyId(Integer policyId);

}