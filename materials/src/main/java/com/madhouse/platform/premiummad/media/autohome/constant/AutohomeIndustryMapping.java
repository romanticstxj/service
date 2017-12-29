package com.madhouse.platform.premiummad.media.autohome.constant;

/**
 * 与汽车之家行业映射
 */
public enum AutohomeIndustryMapping {
	AI1001(1001, 70407),
	AI1002(1002, 70409),
	AI1003(1003, 70602),
	AI1004(1004, 71904),
	AI1005(1005, 71904),
	AI1006(1006, 71999),
	AI1007(1007, 71999),
	AI2001(2001, 72299),
	AI2002(2002, 72299),
	AI2003(2003, 72299),
	AI3001(3001, 70110),
	AI3002(3002, 70107),
	AI3003(3003, 70299),
	AI3004(3004, 70199),
	AI3005(3005, 70299),
	AI3006(3006, 70199),
	AI3007(3007, 70199),
	AI4001(4001, 70212),
	AI4002(4002, 70207),
	AI4003(4003, 70206),
	AI4004(4004, 70299),
	AI4005(4005, 70210),
	AI4006(4006, 70299),
	AI4007(4007, 70299),
	AI5001(5001, 70902),
	AI5002(5002, 70902),
	AI5003(5003, 70901),
	AI5004(5004, 70999),
	AI5005(5005, 70999),
	AI6001(6001, 71199),
	AI6002(6002, 71110),
	AI6003(6003, 71199),
	AI6004(6004, 71114),
	AI6005(6005, 71199),
	AI6006(6006, 71199),
	AI7001(7001, 71213),
	AI7002(7002, 71214),
	AI7003(7003, 71299),
	AI7004(7004, 71213),
	AI7005(7005, 71299),
	AI7006(7006, 71208),
	AI7007(7007, 71212),
	AI7008(7008, 71299),
	AI8001(8001, 72199),
	AI8002(8002, 72105),
	AI8003(8003, 72199),
	AI8004(8004, 72199),
	AI9001(9001, 71699),
	AI9002(9002, 79901),
	AI9003(9003, 79901),
	AI9004(9004, 79901),
	AI10001(10001, 70701),
	AI10002(10002, 70703),
	AI10003(10003, 70714),
	AI10004(10004, 70713),
	AI10005(10005, 70799),
	AI10006(10006, 70799),
	AI10007(10007, 70799),
	AI10008(10008, 70799),
	AI11001(11001, 71006),
	AI11002(11002, 71005),
	AI11003(11003, 71099),
	AI11004(11004, 79901),
	AI11005(11005, 79901),
	AI11006(11006, 79901),
	AI11007(11007, 79901),
	AI12001(12001, 79901),
	AI12002(12002, 79901),
	AI12003(12003, 79901),
	AI12004(12004, 79901),
	AI12005(12005, 71399),
	AI12006(12006, 71301),
	AI12007(12007, 71301),
	AI12008(12008, 71302),
	AI12009(12009, 71303),
	AI12010(12010, 71309),
	AI12011(12011, 71399),
	AI12012(12012, 71399),
	AI13001(13001, 70402),
	AI13002(13002, 70501),
	AI13003(13003, 71905),
	AI13004(13004, 70499),
	AI13005(13005, 70499),
	AI14001(14001, 71402),
	AI14002(14002, 71404),
	AI14003(14003, 71403),
	AI14004(14004, 71499),
	AI15001(15001, 71799),
	AI15002(15002, 71799),
	AI15003(15003, 72611),
	AI15004(15004, 72699),
	AI15005(15005, 79901),
	AI15006(15006, 71599),
	AI15007(15007, 71599),
	AI16001(16001, 79901),
	AI16002(16002, 79901),
	AI17001(17001, 79901);
	
	int industryId;
	int mediaIndustryId;

	AutohomeIndustryMapping(int industryId, int mediaIndustryId) {
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
		for (AutohomeIndustryMapping item : AutohomeIndustryMapping.values()) {
			if (item.getIndustryId() == industryId) {
				return item.getMediaIndustryId();
			}
		}
		// 否则返回其他
		return AutohomeIndustryMapping.AI17001.getMediaIndustryId();
	}

}
