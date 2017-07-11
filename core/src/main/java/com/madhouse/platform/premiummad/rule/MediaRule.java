package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;

public class MediaRule extends BaseRule {

	/**
	 * 媒体是否存在且有效
	 * @param mediaIds
	 * @param uploadedMedias
	 */
	public static void checkMedias(String[] mediaIds, List<SysMedia> uploadedMedias) {
		if (uploadedMedias == null || uploadedMedias.size() != mediaIds.length) {
			throw new BusinessException(StatusCode.SC416, "存在无效的媒体ID" + Arrays.toString(mediaIds));
		}
		List<Integer> invaildMediaIds = new ArrayList<Integer>();
		for (SysMedia media : uploadedMedias) {
			if (media.getStatus() < 1) {
				invaildMediaIds.add(media.getId());
			}
		}
		if (invaildMediaIds.size() > 0) {
			throw new BusinessException(StatusCode.SC416, "以下媒体ID未启用" + Arrays.toString(invaildMediaIds.toArray()));
		}
	}
}
