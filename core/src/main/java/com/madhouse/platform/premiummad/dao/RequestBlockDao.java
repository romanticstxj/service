package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.RequestBlock;

public interface RequestBlockDao {

    int insertSelective(RequestBlock record);

    RequestBlock selectByPrimaryKey(Integer id);
    
    int updateStatus(RequestBlock record);

    List<RequestBlock> queryAllByParams(@Param("type") Integer type);
  
	int checkName(String code);
}