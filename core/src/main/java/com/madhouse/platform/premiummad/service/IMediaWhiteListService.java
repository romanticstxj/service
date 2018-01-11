package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.MediaWhiteList;

public interface IMediaWhiteListService {
	
	void insert(MediaWhiteList mediaWhiteList);
	
	void delete(Integer id);
	
	List<MediaWhiteList> list(Integer mediaCategory);
}
