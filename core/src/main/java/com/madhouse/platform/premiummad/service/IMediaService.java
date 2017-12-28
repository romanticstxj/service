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

	/**
	 * 通过媒体组ID列表及审核模式（素材/广告主）获取媒体ID
	 * 
	 * @param meidaGroupId
	 * @param materialMeidaAuditMode
	 * @return
	 */
	int[] getMeidaIds(String meidaGroups, Byte meidaAuditMode);

	List<Media> queryAll(List<Integer> ids, Integer category);
}
