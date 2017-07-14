package com.madhouse.platform.premiummad.media.sohu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.media.model.SohuCustomerListDetail;
import com.madhouse.platform.premiummad.media.model.SohuCustomerListResponse;
import com.madhouse.platform.premiummad.media.model.SohutvResponse;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuNewsCustomerListApiTask {

	private Logger logger = LoggerFactory.getLogger(SystemConstant.LOG_TASK_OTV);

	@Value("#{'${sohu.datasource}'.split(',')}")
	private List<String> sohuDatasource;

	@Value("${sohu.customer.list}")
	private String cutomerListUrl;

	@Value("#{'${sohu.customer.pageList}'.split(',')}")
	private List<String> pageList;

	@Autowired
	private SohuNewsAuth sohuAuth;

	public void list() {
		logger.info("++++++++++Sohu News get advertiser list begin+++++++++++");
		for (String page : pageList) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("perpage", 50);
			paramMap.put("page", page);
			String request = sohuAuth.setHttpMethod("GET").setApiUrl(cutomerListUrl).setParamMap(paramMap).buildRequest();
			logger.info("SohuNewsCustomerListApiTask.list param page:{} request:{}", page, request);
			String url = cutomerListUrl + "?" + request;
			Map<String, Object> getMap = HttpUtils.get(url);
			String result = (String) getMap.get("responseBody");
			logger.info("SohuNewsCustomerListApiTask.list http get:{}. result json: {}", url, result);
			SohutvResponse sohutvResponse = JSONObject.parseObject(result, SohutvResponse.class);

			for (String dataSource : sohuDatasource) {
				System.err.println("dataSource:" + dataSource);
				if (sohutvResponse != null) {
					if (sohutvResponse.isStatus()) {
						SohuCustomerListResponse sohuCustomerListResponse = JSONObject.parseObject(sohutvResponse.getContent().toString(), SohuCustomerListResponse.class);
						List<SohuCustomerListDetail> sohuCustomerListDetails = sohuCustomerListResponse.getItems();
						System.err.println("sohuCustomerListDetails:" + sohuCustomerListDetails != null ? 0 : sohuCustomerListDetails.size());
					}
				}
			}
		}
	}
}
