package com.madhouse.platform.premiummad.media.autohome;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AutohomeMaterialStatusApiTask {

	@Value("${autohome.creativeUploadUrl}")
	private String creativeUploadUrl;

	@Value("${autohome.dspId}")
	private String dspId;

	@Value("${autohome.dispName}")
	private String dispName;

	@Value("${autohome.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_autohome}")
	private String mediaGroupStr;

	public void getStatus() {
		// TODO
	}
}
