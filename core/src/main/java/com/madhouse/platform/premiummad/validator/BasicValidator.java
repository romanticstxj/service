package com.madhouse.platform.premiummad.validator;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import com.madhouse.platform.premiummad.util.StringUtils;

public class BasicValidator {

	public static boolean validateDigits(Object value, Digits digits) {
		int maxIntegerLength = digits.integer();
		int maxFractionLength = digits.fraction();
		
		//null values are valid
		String s = String.valueOf(value);
		CharSequence cs = (CharSequence) s;
		if ( cs == null ) {
			return true;
		}

		BigDecimal bigNum = getBigDecimalValue( cs );
		if ( bigNum == null ) {
			return false;
		}

		int integerPartLength = bigNum.precision() - bigNum.scale();
		int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();

		return ( maxIntegerLength >= integerPartLength && maxFractionLength >= fractionPartLength );
	}
	
	private static BigDecimal getBigDecimalValue(CharSequence charSequence) {
		BigDecimal bd;
		try {
			bd = new BigDecimal( charSequence.toString() );
		}
		catch (NumberFormatException nfe) {
			return null;
		}
		return bd;
	}

	public static boolean validateNotNull(Object value, Digits digits) {
		return !StringUtils.isEmpty(value);
	}
	
}
