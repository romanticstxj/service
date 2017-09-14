package com.madhouse.platform.premiummad.service;

import java.util.List;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.model.MediaModel;

public interface IMediaService extends IBaseService<Media>{

	//All basic method declarations have been set in base service interface
	
	/**
	 * 获取所有已启用的媒体列表
	 * 
	 * @return
	 */
	List<MediaModel> getAuditedMedia();
}
