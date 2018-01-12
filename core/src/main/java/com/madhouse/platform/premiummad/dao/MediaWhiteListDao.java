package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.MediaWhiteList;

public interface MediaWhiteListDao {

    int insertSelective(MediaWhiteList record);
  
    int delete(Integer id);

    List<MediaWhiteList> list(@Param("mediaCategory") Integer mediaCategory);

	int checkName(Integer mediaId);

	MediaWhiteList selectByPrimaryKey(Integer id);
}