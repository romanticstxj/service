package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Media;

public interface MediaDao {
	
	public Integer insert(Media media);
	
	public Integer checkName(String mediaName);

	public Media queryMediaById(Integer mediaId);

	public List<Media> queryAll(@Param("idStrs") List<Integer> idStrs, @Param("category") Integer category);

	public Integer update(Media media);

	public Integer updateStatus(Media media);
	
}
