package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Dict;
import com.madhouse.platform.premiummad.entity.Location;

public interface DictDao {

	List<Dict> queryAllMediaCategories();

	List<Dict> queryAllAdspaceLayout(Dict dict);

	List<Location> queryAllLocations();
}
