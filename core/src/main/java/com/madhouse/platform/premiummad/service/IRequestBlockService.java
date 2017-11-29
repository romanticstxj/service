package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.RequestBlock;

public interface IRequestBlockService extends IBaseService<RequestBlock>{
	
	List<RequestBlock> list(Integer type);
	
}
