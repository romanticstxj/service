package com.madhouse.platform.smartexchange.holder;

import com.madhouse.platform.smartexchange.constant.SystemConstant;
import com.madhouse.platform.smartexchange.entity.BusinessMaster;
import com.madhouse.platform.smartexchange.service.IBusinessMasterService;
import com.madhouse.platform.smartexchange.service.impl.BusinessMasterServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextHolder {

	public static Map<String, BusinessMaster> businessMasterHolder = new HashMap<>();

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	static {
		if (ContextHolder.businessMasterHolder == null || ContextHolder.businessMasterHolder.isEmpty()) {
			ContextHolder.setCustomerType(SystemConstant.DATASOURCE_SMARTEXCHANGE);
			IBusinessMasterService businessMasterService = ApplicationContextHolder.getBean(BusinessMasterServiceImpl.class);
			if (businessMasterService != null) {
				List<BusinessMaster> businessMasters = businessMasterService.queryAll();
				if (businessMasters != null) {
					for (BusinessMaster businessMaster : businessMasters) {
						if (businessMaster != null) {
							String businessId = businessMaster.getBusinessId();
							String systemId = businessMaster.getSystemId();
							ContextHolder.businessMasterHolder.put(systemId + "_" + businessId, businessMaster);
							ContextHolder.businessMasterHolder.put(businessMaster.getUrl(), businessMaster);
						}
					}
				}
			}
		}
	}

	public static void setCustomerType(String customerType) {
		contextHolder.set(customerType);
	}

	public static String getCustomerType() {
		return contextHolder.get();
	}

	public static void clearCustomerType() {
		contextHolder.remove();
	}
}
