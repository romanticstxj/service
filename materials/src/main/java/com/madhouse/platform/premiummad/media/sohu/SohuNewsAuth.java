package com.madhouse.platform.premiummad.media.sohu;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsAuth {

	private static final String AUTH_CONSUMER_KEY = "auth_consumer_key";

	private static final String AUTH_NONCE = "auth_nonce";

	private static final String AUTH_SIGNATURE_METHOD = "auth_signature_method";

	private static final String AUTH_TIMESTAMP = "auth_timestamp";

	private static final String AUTH_SIGNATURE = "auth_signature";

	private static final String AUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";

	private static final String EQ = "=";
	private static final String AND = "&";

	private String httpMethod;
	private String apiUrl;
	private Map<String, Object> paramMap = new HashMap<>();

	@Value("${sohuNews.key}")
	private String authConsumerKeyValue;

	@Value("${sohuNews.secret}")
	private String secretValue;

	/**
	 * 设置请求方式
	 * 
	 * @param httpMethod
	 *            大写请求方式,如POST,GET
	 * @return this
	 */
	public SohuNewsAuth setHttpMethod(final String httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	/**
	 * 设置请求ApiUrl
	 * 
	 * @param apiUrl
	 *            小写地址url
	 * @return this
	 */
	public SohuNewsAuth setApiUrl(final String apiUrl) {
		this.apiUrl = apiUrl;
		return this;
	}

	/**
	 * 设置业务参数
	 * 
	 * @param paramMap
	 *            Map<String,Object>
	 * @return this
	 */
	public SohuNewsAuth setParamMap(final Map<String, Object> paramMap) {
		this.paramMap = paramMap;
		return this;
	}

	/**
	 * 根据设置的值构建请求字符串
	 * 
	 * @return request
	 */
	public String buildRequest() {
		Assert.hasText(httpMethod, "[Assertion failed] - httpMethod argument must have length; it must not be null or empty");
		Assert.hasText(apiUrl, "[Assertion failed] - apiUrl argument must have length; it must not be null or empty");

		String orderParamsStr = buildOrderParamsStr();
		String authSignature = buildAuthSignature(orderParamsStr);
		return orderParamsStr + AND + AUTH_SIGNATURE + EQ + authSignature;
	}

	/**
	 * 构建authSignature
	 * 
	 * @param orderParamsStr
	 *            业务参数和校验参数字符串
	 * @return authSignature
	 */
	private String buildAuthSignature(String orderParamsStr) {
		byte[] sha1 = HmacUtils.hmacSha1(secretValue, buildText(orderParamsStr));
		return Base64.encodeBase64String(sha1);
	}

	/**
	 * 根据规则构建text HttpMethod大写&encode(接口地址小写)&encode(业务参数和校验参数)
	 * 
	 * @param orderParamsStr
	 *            业务参数和校验参数字符串
	 * @return text
	 */
	private String buildText(String orderParamsStr) {
		return httpMethod.toUpperCase() + AND + StringUtils.encode(apiUrl.toLowerCase()) + AND + StringUtils.encode(orderParamsStr);
	}

	/**
	 * 拼接参数
	 * 
	 * @param paramMap
	 *            排序后的map
	 * @return 拼接后的字符串
	 */
	private String spliceParam(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			String paramKey = entry.getKey();
			Object paramValue = entry.getValue();
			sb.append(paramKey).append(EQ).append(paramValue).append(AND);
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	/**
	 * 业务参数(encode)和校验参数名称升序排列再拼接
	 * 
	 * @return 拼接后的字符串
	 */
	private String buildOrderParamsStr() {
		// 将业务参数类型是string的encode
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String) {
				entry.setValue((StringUtils.encode((String) value)));
			}
		}
		// 加上签名校验参数
		paramMap.put(AUTH_CONSUMER_KEY, authConsumerKeyValue);
		paramMap.put(AUTH_NONCE, getAuthNonce());
		paramMap.put(AUTH_SIGNATURE_METHOD, AUTH_SIGNATURE_METHOD_VALUE);
		paramMap.put(AUTH_TIMESTAMP, getAuthTimestamp());
		return spliceParam(new TreeMap<>(paramMap));// 按参数名称(key值名称)升序排列
	}

	/**
	 * 生成随机串
	 * 
	 * @return 随机字符串
	 */
	private String getAuthNonce() {
		long nonce = System.currentTimeMillis() + new Random().nextInt(100);
		return String.valueOf(nonce);
	}

	/**
	 * 生产UNIX时间戳,容忍时差2分钟
	 * 
	 * @return 秒
	 */
	private Long getAuthTimestamp() {
		return System.currentTimeMillis() / 1000;
	}
}
