package com.madhouse.platform.premiummad.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.dto.ResponseHeaderDto;

/**
 * 设置ResponseDto 
 * 
 * @author Xingjie.Teng
 */
public class ResponseUtils {
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.LOGGER_PREMIUMMAD);

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> lists) {
		return response(sc, lists, null);
	}

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> lists, String message) {
		ResponseDto<T> responseDto = new ResponseDto<>();
		ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto();
		responseDto.setResponseHeaderDto(responseHeaderDto);

		logger.debug("{" + sc.getValue() + " : " + sc.getDescrip() + "}");

		if (sc.getValue() == StatusCode.SC20000.getValue()) { // 设置成功状态
			responseHeaderDto.setResponseCode(SystemConstant.RESPONSECODE_SUCCESS); // 查询成功
			responseDto.setResults(lists);
		} else if (sc.getValue() == StatusCode.SC31001.getValue()) { // 系统错误
			responseHeaderDto.setResponseCode(SystemConstant.RESPONSECODE_FATAL);
			responseDto.setResults(null);
		} else { // 其他错误
			responseHeaderDto.setResponseCode(SystemConstant.RESPONSECODE_ERROR);
			responseDto.setResults(null);
		}

		responseHeaderDto.setErrorCode(sc.getValue());
		responseHeaderDto.setResultsSize(BeanUtils.getListSize(lists));
		if (message != null) {
			responseHeaderDto.setErrorMsg(message);
		} else {
			responseHeaderDto.setErrorMsg(sc.getDescrip());
		}
		return responseDto;

	}
}
