package com.madhouse.platform.premiummad.media.dianping.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.constant.StatusConstant;

@Component
public class DianpingCreateToken {

	@Value("${dianping.dsp_id1}")
	private String dspId1;
	@Value("${dianping.uid1}")
	private int uId1;

	@Value("${dianping.dsp_id2}")
	private String dspId2;
	@Value("${dianping.uid2}")
	private int uId2;

	public String createToken(String brandType) {
		long timeStamp = System.currentTimeMillis() / 1000L;
		String sign = "";
		String plain = "";
		// 判断是保洁还是非保洁
		if (StatusConstant.MATERIAL_BRAND_TYPE_PG.equals(brandType)) {
			sign = DigestUtils.md5Hex(String.format("%s%d", dspId1, timeStamp));
			plain = String.format("%d,%d,%s", uId1, timeStamp, sign);

		} else if (StatusConstant.MATERIAL_BRAND_TYPE_OTHERS.equals(brandType)) {
			sign = DigestUtils.md5Hex(String.format("%s%d", dspId2, timeStamp));
			plain = String.format("%d,%d,%s", uId2, timeStamp, sign);
		}
		String token = Base64.encodeBase64String(plain.getBytes());
		return token;
	}
}
