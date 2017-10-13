package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.AdspaceConstant;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.SysMedia;
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
	public static AdspaceModel buildAdspace(Adspace adspace, List<String> meterialMinesTypes, List<String> coverMinesTypes, List<String> logoMinesTypes, SysMedia media) {
		AdspaceModel adspaceModel = new AdspaceModel();
		BeanUtils.copyProperties(adspace, adspaceModel);
		// 平台类型
		adspaceModel.setOsType(adspace.getTerminalOs());
		// 媒体名称
		adspaceModel.setMediaName(media != null ? media.getName() : "");
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
				video.setSizes(getMaterialListSize(adspace.getMaterialSize(), adspaceModel));
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
			AdspaceModel.Native natives = adspaceModel.new Native();
			if (adspace.getLayout().equals(Layout.LO30011.getValue())) {
				if (adspace.getCoverType() > 0 && !StringUtils.isEmpty(adspace.getCoverSize())) {
					AdspaceModel.Image nativeCover = adspaceModel.new Image();
					nativeCover.setSizes(getMaterialListSize(adspace.getCoverSize(), adspaceModel));
					nativeCover.setMimes(coverMinesTypes);
					natives.setCover(nativeCover);
				}
				natives.setVideo(getVideo(adspace, adspaceModel, meterialMinesTypes));
			} else {
				adspaceModel.setLayout(adspace.getLayout() + adspace.getMaterialCount());
				natives.setImage(getImage(adspace, adspaceModel, meterialMinesTypes));
			}
			
			if (adspace.getLogoType() > 0 && !StringUtils.isEmpty(adspace.getLogoSize())) {
				AdspaceModel.Image nativesIcon = adspaceModel.new Image();
				nativesIcon.setSizes(getMaterialListSize(adspace.getLogoSize(), adspaceModel));
				nativesIcon.setMimes(logoMinesTypes);
				natives.setIcon(nativesIcon);
			}

			natives.setTitle(adspace.getTitleMaxLength());
			natives.setDesc(adspace.getDescMaxLength());
			natives.setContent(adspace.getContentMaxLength());
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
	public static AdspaceModel.Image getImage(Adspace adspace, AdspaceModel adspaceModel, List<String> minesTypes) {
		AdspaceModel.Image img = adspaceModel.new Image();
		img.setSizes(getMaterialListSize(adspace.getMaterialSize(), adspaceModel));
		adspaceModel.setSizes(getMaterialListSize(adspace.getMaterialSize(), adspaceModel));
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
	public static AdspaceModel.Video getVideo(Adspace adspace, AdspaceModel adspaceModel, List<String> minesTypes) {
		AdspaceModel.Video video = adspaceModel.new Video();
		if (!adspace.getLayout().equals(Layout.LO20004.getValue())) {
			video.setSizes(getMaterialListSize(adspace.getMaterialSize(), adspaceModel));
			adspaceModel.setSizes(getMaterialListSize(adspace.getMaterialSize(), adspaceModel));

			String[] nativesdescription = org.springframework.util.StringUtils.tokenizeToStringArray(adspace.getMaterialDuration(), ",");
			video.setMinDuraion(Integer.parseInt(nativesdescription[0]));
			video.setMaxDuration(Integer.parseInt(nativesdescription[1]));
			video.setMimes(minesTypes);
		}
		return video;
	}
	
	/**
	 * size 存在多个，已,分割
	 * 
	 * @param materialSize
	 * @return
	 */
	private static List<AdspaceModel.Size> getMaterialListSize(String materialSize, AdspaceModel adspaceModel) {
		List<AdspaceModel.Size> list = new ArrayList<AdspaceModel.Size>();
		String[] listSize = org.springframework.util.StringUtils.tokenizeToStringArray(materialSize, ",");
		for (String size : listSize) {
			String[] sizes = org.springframework.util.StringUtils.tokenizeToStringArray(size, "*");
			AdspaceModel.Size metaDataSize = adspaceModel.new Size();
			metaDataSize.setW(Integer.parseInt(sizes[0]));
			metaDataSize.setH(Integer.parseInt(sizes[1]));
			list.add(metaDataSize);
		}
		return list;
	}
}
