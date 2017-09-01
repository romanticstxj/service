package com.madhouse.platform.premiummad.media.valuemaker.constant;

/**
 * 与万流客行业映射
 */
public enum ValuekerIndustryMapping {
	AI1001(1001, 5701),
	AI1002(1002, 5702),
	AI1003(1003, 5201),
	AI1004(1004, 7503),
	AI1005(1005, 7507),
	AI1006(1006, 7507),
	AI1007(1007, 7507),
	AI2001(2001, 5904),
	AI2002(2002, 5902),
	AI2003(2003, 5904),
	AI3001(3001, 6101),
	AI3002(3002, 6101),
	AI3003(3003, 6202),
	AI3004(3004, 6101),
	AI3005(3005, 6102),
	AI3006(3006, 6101),
	AI3007(3007, 6101),
	AI4001(4001, 6202),
	AI4002(4002, 6202),
	AI4003(4003, 6201),
	AI4004(4004, 6504),
	AI4005(4005, 6202),
	AI4006(4006, 6504),
	AI4007(4007, 6504),
	AI5001(5001, 5904),
	AI5002(5002, 5904),
	AI5003(5003, 5904),
	AI5004(5004, 5904),
	AI5005(5005, 5904),
	AI6001(6001, 5402),
	AI6002(6002, 5402),
	AI6003(6003, 5402),
	AI6004(6004, 5402),
	AI6005(6005, 5402),
	AI6006(6006, 5402),
	AI7001(7001, 6708),
	AI7002(7002, 7601),
	AI7003(7003, 6706),
	AI7004(7004, 6707),
	AI7005(7005, 6704),
	AI7006(7006, 6701),
	AI7007(7007, 6702),
	AI7008(7008, 6709),
	AI8001(8001, 6902),
	AI8002(8002, 6904),
	AI8003(8003, 6903),
	AI8004(8004, 6906),
	AI9001(9001, 9901),
	AI9002(9002, 9901),
	AI9003(9003, 9901),
	AI9004(9004, 9901),
	AI10001(10001, 7201),
	AI10002(10002, 7201),
	AI10003(10003, 6504),
	AI10004(10004, 6504),
	AI10005(10005, 6504),
	AI10006(10006, 6504),
	AI10007(10007, 6504),
	AI10008(10008, 6504),
	AI11001(11001, 7803),
	AI11002(11002, 7803),
	AI11003(11003, 7801),
	AI11004(11004, 7801),
	AI11005(11005, 7806),
	AI11006(11006, 7806),
	AI11007(11007, 7802),
	AI12001(12001, 8402),
	AI12002(12002, 8402),
	AI12003(12003, 8402),
	AI12004(12004, 8402),
	AI12005(12005, 8402),
	AI12006(12006, 8303),
	AI12007(12007, 8303),
	AI12008(12008, 8303),
	AI12009(12009, 8303),
	AI12010(12010, 8303),
	AI12011(12011, 8303),
	AI12012(12012, 9901),
	AI13001(13001, 7902),
	AI13002(13002, 6601),
	AI13003(13003, 8002),
	AI13004(13004, 5801),
	AI13005(13005, 5801),
	AI14001(14001, 9803),
	AI14002(14002, 9814),
	AI14003(14003, 9901),
	AI14004(14004, 9901),
	AI15001(15001, 7102),
	AI15002(15002, 7101),
	AI15003(15003, 5601),
	AI15004(15004, 5602),
	AI15005(15005, 8402),
	AI15006(15006, 8402),
	AI15007(15007, 8402),
	AI16001(16001, 9901),
	AI16002(16002, 8001),
	AI17001(17001, 9901);
	
	int industryId;
	int mediaIndustryId;

	ValuekerIndustryMapping(int industryId, int mediaIndustryId) {
		this.industryId = industryId;
		this.mediaIndustryId = mediaIndustryId;
	}

	public int getIndustryId() {
		return industryId;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

	public int getMediaIndustryId() {
		return mediaIndustryId;
	}

	public void setMediaIndustryId(int mediaIndustryId) {
		this.mediaIndustryId = mediaIndustryId;
	}

	public static int getMediaIndustryId(int industryId) {
		for (ValuekerIndustryMapping item : ValuekerIndustryMapping.values()) {
			if (item.getIndustryId() == industryId) {
				return item.getMediaIndustryId();
			}
		}
		// 否则返回其他
		return ValuekerIndustryMapping.AI17001.getMediaIndustryId();
	}

}
