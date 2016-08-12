package com.msds.monitor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum BankgatewayInfoEnum {
	ITEM_STATERTIME("startTime","开始时间"),
	ITEM_TRANTYPE("tranType","交易类型"),
	ITEM_CHANNALCODE("payChannelCode","交易渠道"),
	ITEM_STATE("state","状态");
	
	public String item;
	
	public String desc;
}
