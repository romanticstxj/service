package com.madhouse.platform.premiummad.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;

/**
 * 设置ResponseDto 
 * 
 * @author Xingjie.Teng
 */
public class ResponseUtils {
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> lists) {
		return response(sc, lists, null);
	}

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> data, String message) {
		ResponseDto<T> responseDto = new ResponseDto<>();

		logger.debug("{" + sc.getValue() + " : " + sc.getDescrip() + "}" + ", with data:" + data);

		responseDto.setData(data);
		responseDto.setCode(sc.getValue());
		responseDto.setMessage(sc.getDescrip() + (message != null ? "{" + message + "}" : ""));

		return responseDto;

	}
	
	/**
	 * T 为  Void 处理, 无错误信息
	 * @param sc
	 * @param message
	 * @return
	 */
	public static <T> ResponseDto<T> response(StatusCode sc) {
		String message = null;
		return response(sc, message);
	}
	
	/**
	 * T 为  Void 处理，有错误信息
	 * @param sc
	 * @param message
	 * @return
	 */
	public static <T> ResponseDto<T> response(StatusCode sc, String message) {
		ResponseDto<T> responseDto = new ResponseDto<>();

		logger.debug("{" + sc.getValue() + " : " + sc.getDescrip() + "}");

		responseDto.setCode(sc.getValue());
		responseDto.setMessage(message != null ? message : sc.getDescrip());

		return responseDto;
	}
}
