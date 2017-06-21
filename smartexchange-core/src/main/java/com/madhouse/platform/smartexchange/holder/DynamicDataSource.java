package com.madhouse.platform.smartexchange.holder;

import com.madhouse.platform.smartexchange.constant.StatusCode;
import com.madhouse.platform.smartexchange.constant.SystemConstant;
import com.madhouse.platform.smartexchange.entity.BusinessMaster;
import com.madhouse.platform.smartexchange.exception.BusinessException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {

		Object targetDataSources = ContextHolder.getCustomerType();
		if (targetDataSources == null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String url = request.getHeader(SystemConstant.URL);
			BusinessMaster businessMaster = ContextHolder.businessMasterHolder.get(url);
			if (businessMaster == null) {
				throw new BusinessException(StatusCode.SC21015, StatusCode.SC21015.getDescrip());
			}
			String dataSourceBeanId = businessMaster.getDataSourceBeanId();
			ContextHolder.setCustomerType(dataSourceBeanId);
			targetDataSources = dataSourceBeanId;
		}
		return targetDataSources;
	}

}
