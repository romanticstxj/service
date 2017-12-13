package com.madhouse.platform.premiummad.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.madhouse.platform.premiummad.constant.SystemConstant;

public class ReportTaskMapping {
	
	public final static Map<Integer, List<String>> CSV_FIELD_MAPPING = new HashMap<>();
	
    public static Map<String, String> reportTaskProperties;
	
    //为了service包中spring注入用
	public void setReportTaskProperties(Map<String, String> reportTaskProperties) {
		ReportTaskMapping.reportTaskProperties = reportTaskProperties;
	}
    
	static {
		List<String> csvFields = new ArrayList<>();
		csvFields.add("mediaName");
		csvFields.add("adspaceName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("incomeStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("mediaName");
		csvFields.add("dateStr");
//		csvFields.add("hour");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("incomeStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_DATEHOUR, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("mediaName");
		csvFields.add("adspaceName");
		csvFields.add("countryName");
		csvFields.add("provinceName");
		csvFields.add("cityName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("incomeStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE_LOCATION, csvFields);
		
		//dsp报表字段映射常量
		csvFields = new ArrayList<>();
		csvFields.add("dspName");
		csvFields.add("dateStr");
//		csvFields.add("hour");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("costStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_DSP_DATEHOUR, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("dspName");
		csvFields.add("countryName");
		csvFields.add("provinceName");
		csvFields.add("cityName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("costStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_DSP_LOCATION, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("dspName");
		csvFields.add("mediaName");
		csvFields.add("adspaceName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("ecpmStr");
		csvFields.add("ecpcStr");
		csvFields.add("costStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_DSP_ADSPACE, csvFields);
		
		//策略报表字段映射常量
		csvFields = new ArrayList<>();
		csvFields.add("policyName");
		csvFields.add("policyDealId");
		csvFields.add("policyTypeName");
		csvFields.add("dspName");
		csvFields.add("dateStr");
//		csvFields.add("hour");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("incomeStr");
		csvFields.add("costStr");
		csvFields.add("priceDiffStr");
		csvFields.add("profitRateStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_POLICY_DATEHOUR, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("policyName");
		csvFields.add("policyDealId");
		csvFields.add("policyTypeName");
		csvFields.add("dspName");
		csvFields.add("countryName");
		csvFields.add("provinceName");
		csvFields.add("cityName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("incomeStr");
		csvFields.add("costStr");
		csvFields.add("priceDiffStr");
		csvFields.add("profitRateStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_POLICY_LOCATION, csvFields);
		
		csvFields = new ArrayList<>();
		csvFields.add("policyName");
		csvFields.add("policyDealId");
		csvFields.add("policyTypeName");
		csvFields.add("dspName");
		csvFields.add("mediaName");
		csvFields.add("adspaceName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
		csvFields.add("timeouts");
		csvFields.add("bidsRateStr");
		csvFields.add("imps");
		csvFields.add("vimps");
		csvFields.add("clks");
		csvFields.add("vclks");
		csvFields.add("vclksRateStr");
		csvFields.add("incomeStr");
		csvFields.add("costStr");
		csvFields.add("priceDiffStr");
		csvFields.add("profitRateStr");
		CSV_FIELD_MAPPING.put(SystemConstant.DB.REPORT_TASK_TYPE_POLICY_ADSPACE, csvFields);
	}
	
}
