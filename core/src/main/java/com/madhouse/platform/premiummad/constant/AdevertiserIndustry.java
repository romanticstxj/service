package com.madhouse.platform.premiummad.constant;

/**
 * 广告主所属行业
 */
public enum AdevertiserIndustry {
	AI1001(1001,"电脑"),
	AI1002(1002,"配件及外设"),
	AI1003(1003,"办公及耗材"),
	AI1004(1004,"软件产品 软件产品"),
	AI1005(1005,"技术服务 技术服务"),
	AI1006(1006,"IT 产品类企业形象"),
	AI1007(1007,"其他 IT 产品类"),
	AI2001(2001,"房地产类企业形象"),
	AI2002(2002,"房产"),
	AI2003(2003,"其它房地产类"),
	AI3001(3001,"运动服饰"),
	AI3002(3002,"商务与休闲装"),
	AI3003(3003,"饰 品与配饰品与配"),
	AI3004(3004,"内衣"),
	AI3005(3005,"鞋类"),
	AI3006(3006,"服饰类企业形象"),
	AI3007(3007,"其他服饰类"),
	AI4001(4001,"手表"),
	AI4002(4002,"眼镜"),
	AI4003(4003,"箱包"),
	AI4004(4004,"剃须产品"),
	AI4005(4005,"珠宝首饰"),
	AI4006(4006,"个人用品类企业形象"),
	AI4007(4007,"其他个人用品类"),
	AI5001(5001,"家居装饰"),
	AI5002(5002,"装饰服务"),
	AI5003(5003,"家居卖场"),
	AI5004(5004,"家居装饰类企业形象"),
	AI5005(5005,"其他家居装饰类"),
	AI6001(6001,"乘用车"),
	AI6002(6002,"商用车"),
	AI6003(6003,"汽车服务"),
	AI6004(6004,"汽车零配件及周边"),
	AI6005(6005,"交通类企业形象"),
	AI6006(6006,"其他交通类"),
	AI7001(7001,"国内院校"),
	AI7002(7002,"留学出国"),
	AI7003(7003,"语言培训"),
	AI7004(7004,"专业培训"),
	AI7005(7005,"教育出国类企业形象"),
	AI7006(7006,"婴幼儿教育"),
	AI7007(7007,"儿童教育"),
	AI7008(7008,"其他教育培训类"),
	AI8001(8001,"银行产品及服务"),
	AI8002(8002,"投资理财产品及服务"),
	AI8003(8003,"金融服务类企业形象"),
	AI8004(8004,"其它金融服务类"),
	AI9001(9001,"服务业"),
	AI9002(9002,"零售业"),
	AI9003(9003,"零售及服务类企业形象"),
	AI9004(9004,"其它零售及服务类"),
	AI10001(10001,"护肤品"),
	AI10002(10002,"化妆品"),
	AI10003(10003,"卫浴用品"),
	AI10004(10004,"口腔护理"),
	AI10005(10005,"卫生用品"),
	AI10006(10006,"洗涤用品"),
	AI10007(10007,"日化类企业形象"),
	AI10008(10008,"其他日化类"),
	AI11001(11001,"酒精饮料"),
	AI11002(11002,"软饮料"),
	AI11003(11003,"食品"),
	AI11004(11004,"烟草与茶叶"),
	AI11005(11005,"餐饮服务"),
	AI11006(11006,"食品饮料类企业形象"),
	AI11007(11007,"其它食品饮料类"),
	AI12001(12001,"娱乐"),
	AI12002(12002,"购物"),
	AI12003(12003,"资讯"),
	AI12004(12004,"生活"),
	AI12005(12005,"企业网络服务"),
	AI12006(12006,"大型多人在线游戏"),
	AI12007(12007,"休闲游戏"),
	AI12008(12008,"网页游戏"),
	AI12009(12009,"单机游戏"),
	AI12010(12010,"游戏平台"),
	AI12011(12011,"网络游戏类企业形象"),
	AI12012(12012,"其他网络服务类"),
	AI13001(13001,"数码影像"),
	AI13002(13002,"家用电器"),
	AI13003(13003,"通信产品"),
	AI13004(13004,"消费类电子企业形象"),
	AI13005(13005,"电子其他消费类"),
	AI14001(14001,"药品 及保健药品"),
	AI14002(14002,"医疗机构"),
	AI14003(14003,"医疗服务类企业形象"),
	AI14004(14004,"其它医疗服务类"),
	AI15001(15001,"旅游酒店"),
	AI15002(15002,"公园 与游乐"),
	AI15003(15003,"出版品"),
	AI15004(15004,"传媒与文化"),
	AI15005(15005,"休闲用品"),
	AI15006(15006,"娱乐及休闲类企业形象"),
	AI15007(15007,"其他娱乐及休闲类"),
	AI16001(16001,"运营商"),
	AI16002(16002,"其他通信服务类"),
	AI17001(17001,"其他");
	
	int value;
	String descrip;

	AdevertiserIndustry(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}
	
	public static String getDescrip(int value) {
		for (AdevertiserIndustry item : AdevertiserIndustry.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
