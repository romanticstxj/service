package com.madhouse.platform.premiummad.service;

import java.util.List;

public interface IBaseService<T> {
	
	int insert(T t);
	
	int update(T t);
	
	int updateStatus(T t);
	
	T queryById(Integer id);
	
	List<T> queryAll(List<Integer> ids);
	
	int checkName(String mediaName);
}
