package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Dict;
import com.madhouse.platform.premiummad.entity.Location;

public interface IDictService {

	List<Dict> queryAllMediaCategories();

	List<Dict> queryAllAdspaceLayout(Dict dict);

	List<Location> queryAllLocations();
	
	List<Dict> queryAllReqBlockTypeList();
}
