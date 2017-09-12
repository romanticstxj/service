package com.madhouse.platform.premiummad.service;

import java.util.List;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.model.MediaModel;

public interface IMediaService extends IBaseService<Media>{

	//All basic method declarations have been set in base service interface
	
	/**
	 * 获取指定dsp下已审核的所有媒体
	 * 
	 * @param dspId
	 * @return
	 */
	List<MediaModel> getAuditedMedia(String dspId);
}
