package com.madhouse.platform.premiummad.util;

import java.text.DecimalFormat;

public class AlgorithmUtils {
	
	public static double calPercent(double d1, double d2){
		double percent = 0.0;
		if(d2 > 0){
			percent = d1 / d2;
		}
		return percent;
	}
	
	public static String convertPercent(double percent){
		DecimalFormat decimalFormat=new DecimalFormat();
		decimalFormat.applyPattern("###.##%");
		String percentStr = decimalFormat.format(percent);
		return percentStr;
	}
	
	/**
	 * 返回两位小数
	 * @param d
	 * @return
	 */
	public static double calIncome(double d){
		DecimalFormat decimalFormat=new DecimalFormat();
		decimalFormat.applyPattern("###########.##");
		double income = d / 100000;
		String formattedIncomeStr = decimalFormat.format(income);
		double formattedIncome = Double.valueOf(formattedIncomeStr);
		return formattedIncome;
	}
	
	public static String convertCurrency(double currency){
		DecimalFormat decimalFormat=new DecimalFormat();
		decimalFormat.applyPattern("¥###########.##");
		String currencyStr = decimalFormat.format(currency);
		return currencyStr;
	}
	

	/**
	 * ecpm=媒体收入/曝光数 * 1000
	 * @param income
	 * @param vimps
	 * @return
	 */
	public static double calEcpm(double income, double vimps) {
		double ecpm = 0.0;
		if(vimps > 0){
			ecpm = income * 1000 / vimps;
		}
		return ecpm;
	}

	public static double calEcpc(double income, double vclks) {
		double ecpc = 0.0;
		if(vclks > 0){
			ecpc = income / vclks;
		}
		return ecpc;
	}

	public static double calPriceDiff(double cost, double income) {
		double priceDiff = 0;
		if(cost > 0){
			priceDiff = cost - income;
		}
		return priceDiff;
	}
	
	
}
