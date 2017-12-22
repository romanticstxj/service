package com.madhouse.platform.premiummad.media.autohome.constant;

/**
 * 与汽车之家行业映射 TODO
 */
public enum AutohomeIndustryMapping {
	AI1001(1001, 901),
	AI1002(1002, 906),
	AI1003(1003, 906),
	AI1004(1004, 401),
	AI1005(1005, 405),
	AI1006(1006, 507),
	AI1007(1007, 507),
	AI2001(2001, 2801),
	AI2002(2002, 2801),
	AI2003(2003, 2801),
	AI3001(3001, 1602),
	AI3002(3002, 1206),
	AI3003(3003, 1206),
	AI3004(3004, 1302),
	AI3005(3005, 1601),
	AI3006(3006, 1206),
	AI3007(3007, 1206),
	AI4001(4001, 1702),
	AI4002(4002, 1703),
	AI4003(4003, 1503),
	AI4004(4004, 1606),
	AI4005(4005, 1701),
	AI4006(4006, 1606),
	AI4007(4007, 1606),
	AI5001(5001, 1206),
	AI5002(5002, 3503),
	AI5003(5003, 3503),
	AI5004(5004, 3503),
	AI5005(5005, 3503),
	AI6001(6001, 2902),
	AI6002(6002, 2902),
	AI6003(6003, 2902),
	AI6004(6004, 2902),
	AI6005(6005, 2902),
	AI6006(6006, 2902),
	AI7001(7001, 505),
	AI7002(7002, 509),
	AI7003(7003, 506),
	AI7004(7004, 504),
	AI7005(7005, 509),
	AI7006(7006, 501),
	AI7007(7007, 502),
	AI7008(7008, 510),
	AI8001(8001, 605),
	AI8002(8002, 611),
	AI8003(8003, 605),
	AI8004(8004, 605),
	AI9001(9001, 1606),
	AI9002(9002, 1606),
	AI9003(9003, 1606),
	AI9004(9004, 1606),
	AI10001(10001, 801),
	AI10002(10002, 805),
	AI10003(10003, 3704),
	AI10004(10004, 3704),
	AI10005(10005, 3702),
	AI10006(10006, 3704),
	AI10007(10007, 3702),
	AI10008(10008, 3702),
	AI11001(11001, 1106),
	AI11002(11002, 1104),
	AI11003(11003, 2610),
	AI11004(11004, 1107),
	AI11005(11005, 2602),
	AI11006(11006, 1101),
	AI11007(11007, 1101),
	AI12001(12001, 2605),
	AI12002(12002, 2605),
	AI12003(12003, 2605),
	AI12004(12004, 2605),
	AI12005(12005, 2605),
	AI12006(12006, 402),
	AI12007(12007, 402),
	AI12008(12008, 402),
	AI12009(12009, 402),
	AI12010(12010, 402),
	AI12011(12011, 402),
	AI12012(12012, 403),
	AI13001(13001, 905),
	AI13002(13002, 1003),
	AI13003(13003, 902),
	AI13004(13004, 1606),
	AI13005(13005, 1606),
	AI14001(14001, 2101),
	AI14002(14002, 2104),
	AI14003(14003, 2105),
	AI14004(14004, 2104),
	AI15001(15001, 701),
	AI15002(15002, 1606),
	AI15003(15003, 3003),
	AI15004(15004, 1606),
	AI15005(15005, 1606),
	AI15006(15006, 2605),
	AI15007(15007, 2605),
	AI16001(16001, 2202),
	AI16002(16002, 1606),
	AI17001(17001, 1606);
	
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
