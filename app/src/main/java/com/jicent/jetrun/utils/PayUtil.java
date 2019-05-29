package com.jicent.jetrun.utils;

import android.app.Activity;

import com.rmc.Util;

public class PayUtil {
	public interface IPayCallback {
		public void onPayFinish(boolean isPayOk);
	}

	public enum PayType {
		getAllCard,//获取全部卡片10元
		coinGift,//金币礼包10元
		propGift,//道具礼包8元
		role1,//角色2，6元 每1000米获得一个护盾
		role2,//角色3，8元 复活一次
		role3,//角色4，8元 开场飞行
		role4,//角色5，10元 永久吸金币
		pet0,//角色1宠物6元  金币加成5%
		pet1,//角色2宠物6元  金币加成10% 
		pet2,//角色3宠物6元  金币加成15%
		pet3,//角色4宠物6元  金币加成20%,分数加成5%
		pet4,//角色5宠物6元  金币加成25%,分数加成10%
	}

	public static void pay(Activity activity, PayType paytype, IPayCallback callback) {
		callback.onPayFinish(true);
	}
	
	public enum PlatformType {
		ENone, EHe, EMm, EWo, EEgame
	}

	private static final boolean IS_HE = true;

	public static PlatformType gePlatformType(Activity activity) {
		PlatformType ret = PlatformType.ENone;
		final int opType = Util.getOperatorType(activity);
		switch (opType) {
		case Util.CHINA_MOBILE:
			ret = IS_HE ? PlatformType.EHe : PlatformType.EMm;
			break;
		case Util.CHINA_UNION:
			ret = PlatformType.EWo;
			break;
		case Util.CHINA_TELECOM:
			ret = PlatformType.EEgame;
			break;
		}
		return ret;
	}
}
