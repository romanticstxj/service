package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.entity.AuditedAdspaceQueryParam;
import com.madhouse.platform.premiummad.entity.DspMedia;

public class DspMediaRule {

	/**
	 * 解析有权限的媒体ID和广告位ID
	 * 
	 * @param dspMedias
	 * @return
	 */
	public static void splitMeidaAndAds(AuditedAdspaceQueryParam queryParam, List<DspMedia> dspMedias) {
		// 设置变量
		List<Integer> mediaIds = new ArrayList<Integer>();
		List<Integer> adspaceIds = new ArrayList<Integer>();

		// 解析媒体和广告位权限
		for (DspMedia dspMedia : dspMedias) {
			// -1 表示所有媒体都有权限
			if (dspMedia.getMediaId().intValue() == -1) {
				mediaIds.clear();
				adspaceIds.clear();
				break;
			} else {
				// adspaceId 为-1表示该媒体下所有广告位都有权限
				if (dspMedia.getAdspaceId() == -1) {
					mediaIds.add(dspMedia.getMediaId());
				} else {
					adspaceIds.add(dspMedia.getAdspaceId());
				}
			}
		}

		queryParam.setMediaIds(mediaIds);
		queryParam.setAdspaceIds(adspaceIds);
	}
}
