package com.madhouse.platform.premiummad.validator;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.madhouse.platform.premiummad.annotation.NotNullValid;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.exception.BusinessException;

public class NotNullValidator implements ConstraintValidator<NotNullValid, Object> {
	private String message;

	public void initialize(NotNullValid constraintAnnotation) {
		this.message = constraintAnnotation.message();
	}

	public boolean isValid(Object value) {
		if (value == null) {
			throw new BusinessException(StatusCode.SC400, this.message);
		}
		if ((value instanceof String)) {
			if (StringUtils.isBlank(value.toString()))
				throw new BusinessException(StatusCode.SC400, this.message);
		} else if ((value instanceof List)) {
			if (((List) value) == null || ((List) value).isEmpty()) {
				throw new BusinessException(StatusCode.SC400, this.message);
			}
		} else if (((value instanceof Map))) {
			if (((Map) value) == null || ((Map) value).isEmpty()) {
				throw new BusinessException(StatusCode.SC400, this.message);
			}
		}

		return true;
	}
}