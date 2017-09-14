package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;
import com.madhouse.platform.premiummad.constant.AdspaceConstant;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.model.AdspaceModel;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class AdspaceRule {

	/**
	 * 根据类型获取支持的类型
	 * 
	 * @param type
	 * @return
	 */
	public static List<Integer> getTypes(Integer type) {
		if (type == null || type.intValue() <= 0) {
			return null;
		}
		List<Integer> result = new ArrayList<Integer>();
		Integer not = 1;
		while (not <= type) {
			if ((not & type) > 0) {
				result.add(not);
			}
			not *= 2;
		}
		return result;
	}

	/**
	 * 构建广告位对象
	 * 
	 * @param adspace
	 * @param meterialMinesTypes
	 * @param coverMinesTypes
	 * @param logoMinesTypes
	 * @return
	 */
	public static AdspaceModel buildAdspace(Adspace adspace, List<String> meterialMinesTypes, List<String> coverMinesTypes, List<String> logoMinesTypes) {
		AdspaceModel adspaceModel = new AdspaceModel();
		BeanUtils.copyProperties(adspace, adspaceModel);
		// 广告类型(1: 普通硬广, 2: 视频, 3: 原生)
		switch (adspace.getAdType()) {
		case AdspaceConstant.PlcmtType.BANNER:
			adspaceModel.setBanner(getImage(adspace, adspaceModel, meterialMinesTypes));
			break;
		case AdspaceConstant.PlcmtType.VIDEO:
			AdspaceModel.Video video = getVideo(adspace, adspaceModel, meterialMinesTypes);
			if (adspace.getLayout() == Layout.LO20001.getValue()) {
				video.setStartDelay(AdspaceConstant.StartDelay.BEFORE_COMMENCEMENT);
				video.setLinearity(AdspaceConstant.Linearity.PATCH_VIDEO);
			}
			if (adspace.getLayout() == Layout.LO20002.getValue()) {
				video.setStartDelay(AdspaceConstant.StartDelay.IN_THE_POST);
				video.setLinearity(AdspaceConstant.Linearity.PATCH_VIDEO);
			}
			if (adspace.getLayout() == Layout.LO20003.getValue()) {
				video.setStartDelay(AdspaceConstant.StartDelay.POST);
				video.setLinearity(AdspaceConstant.Linearity.PATCH_VIDEO);
			}
			if (adspace.getLayout() == Layout.LO20004.getValue()) {
				String[] videoMaterialSize = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getMaterialSize(), "*");
				video.setW(Integer.parseInt(videoMaterialSize[0]));
				video.setH(Integer.parseInt(videoMaterialSize[1]));
				video.setMimes(meterialMinesTypes);
				video.setLinearity(AdspaceConstant.Linearity.SUSPENSION_PAUSE);
			}
			if (adspace.getLayout() == Layout.LO20005.getValue()) {
				video.setLinearity(AdspaceConstant.Linearity.BOOT_VIDEO);
			}
			if (adspace.getLayout() == Layout.LO20006.getValue()) {
				video.setLinearity(AdspaceConstant.Linearity.SHUTDOWN_VIDEO);
			}
			if (adspace.getLayout() == Layout.LO20007.getValue()) {
				video.setLinearity(AdspaceConstant.Linearity.SCREEN_VIDEO);
			}
			adspaceModel.setVideo(video);
			break;
		case AdspaceConstant.PlcmtType.NATIVE:
			AdspaceModel.Native natives = new AdspaceModel.Native();
			if (adspace.getLayout().equals(Layout.LO30004.getValue())) {
				if (adspace.getCoverType() > 0 && !StringUtils.isEmpty(adspace.getCoverSize())) {
					AdspaceModel.Image nativeCover = new AdspaceModel.Image();
					String[] videoSize = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getCoverSize(), "*");
					nativeCover.setW(Integer.parseInt(videoSize[0]));
					nativeCover.setH(Integer.parseInt(videoSize[1]));
					nativeCover.setMimes(coverMinesTypes);
					natives.setCover(nativeCover);
				}
				natives.setVideo(getVideo(adspace, adspaceModel, meterialMinesTypes));
			} else {
				adspaceModel.setLayout(adspace.getLayout() + adspace.getMaterialCount());
				natives.setImage(getImage(adspace, adspaceModel, meterialMinesTypes));
			}

			AdspaceModel.Image nativesIcon = new AdspaceModel.Image();
			if (adspace.getLogoType() > 0 && !StringUtils.isEmpty(adspace.getLogoSize())) {
				String[] nativesIconSize = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getLogoSize(), "*");
				nativesIcon.setW(Integer.parseInt(nativesIconSize[0]));
				nativesIcon.setH(Integer.parseInt(nativesIconSize[1]));
				nativesIcon.setMimes(logoMinesTypes);
			}

			natives.setIcon(nativesIcon);
			natives.setTitle(adspace.getTitleMaxLength() > 0 ? adspace.getTitleMaxLength() : 0);
			natives.setDesc(adspace.getDescMaxLength() > 0 ? adspace.getDescMaxLength() : 0);
			natives.setContent(adspace.getContentMaxLength() > 0 ? adspace.getContentMaxLength() : 0);
			adspaceModel.setNatives(natives);
			break;
		}
		return adspaceModel;
	}

	/**
	 * 构建 image 对象
	 * 
	 * @param adspace
	 * @param metaData
	 * @param minesTypes
	 * @return
	 */
	public static AdspaceModel.Image getImage(Adspace adspace, AdspaceModel metaData, List<String> minesTypes) {
		AdspaceModel.Image img = new AdspaceModel.Image();
		String[] str = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getMaterialSize(), "*");
		img.setW(Integer.parseInt(str[0]));
		img.setH(Integer.parseInt(str[1]));

		metaData.setW(Integer.parseInt(str[0]));
		metaData.setH(Integer.parseInt(str[1]));

		img.setMimes(minesTypes);
		return img;
	}

	/**
	 * 构建 video 对象
	 * 
	 * @param adspace
	 * @param metaData
	 * @param minesTypes
	 * @return
	 */
	public static AdspaceModel.Video getVideo(Adspace adspace, AdspaceModel metaData, List<String> minesTypes) {
		AdspaceModel.Video video = new AdspaceModel.Video();
		if (!adspace.getLayout().equals(Layout.LO20004.getValue())) {
			String[] videoSize = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getMaterialSize(), "*");
			video.setW(Integer.parseInt(videoSize[0]));
			video.setH(Integer.parseInt(videoSize[1]));

			metaData.setW(Integer.parseInt(videoSize[0]));
			metaData.setH(Integer.parseInt(videoSize[1]));

			String[] nativesdescription = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getMaterialDuration(), ",");
			video.setMinDuraion(Integer.parseInt(nativesdescription[0]));
			video.setMaxDuration(Integer.parseInt(nativesdescription[1]));
			video.setMimes(minesTypes);
		}
		return video;
	}
}
