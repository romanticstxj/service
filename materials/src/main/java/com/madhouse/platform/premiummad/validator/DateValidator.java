package com.madhouse.platform.premiummad.validator;

import java.text.ParseException;
import java.util.Date;
import com.madhouse.platform.premiummad.annotation.DateFormatValid;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.DateUtils;

public class DateValidator implements ConstraintValidator<DateFormatValid, Object> {
	private String message;
	private String format;

	public void initialize(DateFormatValid constraintAnnotation) {
		this.message = constraintAnnotation.message();
		this.format = constraintAnnotation.format();
	}

	public boolean isValid(Object value) {
		if (value == null) {
			return true;
		}

		Date date;
		try {
			date = DateUtils.getFormatDateByPattern(this.format, value.toString());
			if (date == null) {
				throw new BusinessException(StatusCode.SC400, this.message);
			}
		} catch (ParseException e) {
			throw new BusinessException(StatusCode.SC400, "日期格式无法解析");
		}
		return true;
	}
}