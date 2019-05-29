package com.jicent.jetrun.data;

public class StaticVariable {
	public static boolean useLocalMusicSetting = true;
	public static boolean isMusicOn;
	public static boolean isSoundOn;
	
	//排行名字
	public static String name;
	public static int myOrder;
	public static boolean nameOk=false;
	
	
	public static int rewardDistance;//奖励的距离
	public static int bestScore;//
	
	public static long loginTime;//上次登录的时间
	
	//道具个数
	public static int speedUpCount;
	public static int gunCount;
	public static int mountCount;
	public static int shieldCount;
	public static int attractCount;
	
	
	/**
	 * 已经登录到的天数,从0开始
	 */
	public static int currDay;
	/**
	 * 金币数
	 */
	public static int coinNum;
	/**
	 * 角色种类
	 */
	public static int roleKind;
	
	
	
	public static final int role0=0;//无技能
	public static final int role1=1;//1000米获得一个护盾
	public static final int role2=2;//复活一次
	public static final int role3=3;//开场飞行1000
	public static final int role4=4;//永久吸金币
	
	//人物是否已经购买
	public static final boolean isBuyFreeMario = true;
	public static boolean isBuyRole1 = false;
	public static boolean isBuyRole2 = false;
	public static boolean isBuyRole3 = false;
	public static boolean isBuyRole4 = false;
	
	//是否购买pet
	public static boolean isBuyPet0 = false;//金币加成5%
	public static boolean isBuyPet1 = false;//金币加成10%
	public static boolean isBuyPet2 = false;//金币加成15%
	public static boolean isBuyPet3 = false;//金币加成20%，分数加成5%
	public static boolean isBuyPet4 = false;//金币加成25%，分数加成10%
	
}
