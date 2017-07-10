package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.OperationResultModel;

public class MediaRule extends BaseRule {

	/**
	 * 媒体是否存在且有效
	 * @param mediaIds
	 * @param uploadedMedias
	 * @param operationResult
	 */
	public static void checkMedias(String[] mediaIds, List<SysMedia> uploadedMedias, OperationResultModel operationResult) {
		if (uploadedMedias == null || uploadedMedias.size() != mediaIds.length) {
			operationResult.setSuccessful(Boolean.FALSE);
			operationResult.setErrorMessage("存在无效的媒体ID[mediaIds=" + Arrays.toString(mediaIds) + "]");
			return;
		}
		List<Integer> invaildMediaIds = new ArrayList<Integer>();
		for (SysMedia media : uploadedMedias) {
			if (media.getStatus() < 1) {
				invaildMediaIds.add(media.getId());
			}
		}
		if (invaildMediaIds.size() > 0) {
			operationResult.setSuccessful(Boolean.FALSE);
			operationResult.setErrorMessage("以下媒体ID未启用[invalidMediaIds=" + Arrays.toString(invaildMediaIds.toArray()) + "]");
			return;
		}
	}
}
