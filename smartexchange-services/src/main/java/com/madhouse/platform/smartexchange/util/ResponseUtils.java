package com.madhouse.platform.smartexchange.util;

import com.madhouse.platform.smartexchange.constant.StatusCode;
import com.madhouse.platform.smartexchange.constant.SystemConstant;
import com.madhouse.platform.smartexchange.dto.ResponseDto;
import com.madhouse.platform.smartexchange.dto.ResponseHeaderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 设置ResponseDto 2015年2月4日上午10:20:31
 * 
 * @author xiejun
 */
public class ResponseUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.LOGGER_SMARTEXCHANGE);

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> lists) {
		return response(sc, lists, null);
	}

	public static <T> ResponseDto<T> response(StatusCode sc, List<T> lists, String message) {
		ResponseDto<T> responseDto = new ResponseDto<>();
		ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto();
		responseDto.setResponseHeaderDto(responseHeaderDto);

		LOGGER.debug("{" + sc.getValue() + " : " + sc.getDescrip() + "}");

		if (sc.getValue() == 20000) { // 设置成功状态
			responseHeaderDto.setResponseCode(SystemConstant.RESPONSECODE_SUCCESS); // 查询成功
			responseDto.setResults(lists);
		} else if (sc.getValue() == 31001) { // 系统错误
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
