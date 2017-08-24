package com.madhouse.platform.premiummad.media.constant;

/**
 * 与乐视行业映射 TODO
 */
public enum ValuekerIndustryMapping {
	AI1001(1001, 11),
	AI1002(1002, 11),
	AI1003(1003, 11),
	AI1004(1004, 11),
	AI1005(1005, 11),
	AI1006(1006, 11),
	AI1007(1007, 11),
    AI2001(2001, 18),
	AI2002(2002, 18),
	AI2003(2003, 18),
	AI3001(3001, 5),
	AI3002(3002, 5),
	AI3003(3003, 5),
    AI3004(3004, 5),
	AI3005(3005, 5),
	AI3006(3006, 5),
	AI3007(3007, 5),
	AI4001(4001, 16),
	AI4002(4002, 16),
	AI4003(4003, 16),
	AI4004(4004, 16),
	AI4005(4005, 16),
	AI4006(4006, 16),
	AI4007(4007, 16),
	AI5001(5001, 9),
	AI5002(5002, 9),
	AI5003(5003, 9),
	AI5004(5004, 9),
	AI5005(5005, 9),
	AI6001(6001, 6),
	AI6002(6002, 6),
	AI6003(6003, 6),
	AI6004(6004, 6),
	AI6005(6005, 6),
	AI6006(6006, 6),
	AI7001(7001, 14),
	AI7002(7002, 14),
	AI7003(7003, 14),
	AI7004(7004, 14),
	AI7005(7005, 14),
	AI7006(7006, 14),
	AI7007(7007, 14),
	AI7008(7008, 14),
	AI8001(8001, 13),
	AI8002(8002, 13),
	AI8003(8003, 13),
	AI8004(8004, 13),
	AI9001(9001, 12),
	AI9002(9002, 1),
	AI9003(9003, 1),
	AI9004(9004, 1),
	AI10001(10001, 4),
	AI10002(10002, 4),
	AI10003(10003, 4),
	AI10004(10004, 3),
	AI10005(10005, 3),
	AI10006(10006, 3),
	AI10007(10007, 3),
	AI10008(10008, 3),
	AI11001(11001, 2),
	AI11002(11002, 2),
	AI11003(11003, 2),
	AI11004(11004, 24),
	AI11005(11005, 19),
	AI11006(11006, 19),
	AI11007(11007, 19),
	AI12001(12001, 17),
	AI12002(12002, 17),
	AI12003(12003, 1),
	AI12004(12004, 17),
	AI12005(12005, 8),
	AI12006(12006, 15),
	AI12007(12007, 15),
	AI12008(12008, 15),
	AI12009(12009, 15),
	AI12010(12010, 15),
	AI12011(12011, 15),
	AI12012(12012, 8),
	AI13001(13001, 11),
	AI13002(13002, 10),
	AI13003(13003, 11),
	AI13004(13004, 11),
	AI13005(13005, 11),
	AI14001(14001, 21),
	AI14002(14002, 22),
	AI14003(14003, 22),
	AI14004(14004, 22),
	AI15001(15001, 15),
	AI15002(15002, 15),
	AI15003(15003, 1),
	AI15004(15004, 1),
	AI15005(15005, 1),
	AI15006(15006, 17),
	AI15007(15007, 17),
	AI16001(16001, 1),
	AI16002(16002, 12),
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
