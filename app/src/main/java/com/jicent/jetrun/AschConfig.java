package com.jicent.jetrun;

public class AschConfig {

	public static final String ContractURL = "http://39.107.143.215:4096";
	public static String Url = "http://39.107.143.215:4096/peer/transactions";
	public static String secret = "boat salute minute kiwi sniff improve extra used rail upon leader phone";
	public static String secondSecret = "";
	public static String gameContractName = "girlhero24";
	public static String contractName = "votenode45";
	public static final String voteForNodeName = "asch_g27";
	public static long oneLevelVote = 20000;
	public static long twoLevelVote = 10000;
	public static long threeLevelVote = 5000;
	public static long fourLevelVote = 2000;
	
	public static String getInviteCodeLegalUrl(){
		return ContractURL + "/api/v2/contracts/" + contractName + "/constant/isInviteCodeExist";
	}
	
	public static String getAddressLegalUrl(String address){
		return ContractURL + "/api/accounts/delegates?address=" + address + "&orderBy=rate:asc&limit=101&offset=0";
	}
	
	public static String getPersonalDataUrl(String address){
		return ContractURL + "/api/v2/accounts/" + address;
	}
}
