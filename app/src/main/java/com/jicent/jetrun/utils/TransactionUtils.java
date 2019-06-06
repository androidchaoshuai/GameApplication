package com.jicent.jetrun.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jicent.jetrun.AschConfig;
import com.jicent.jetrun.http.ParameterMap;
import com.jicent.jetrun.http.REST;
import com.jicent.jetrun.transaction.TransactionBuilder;
import com.jicent.jetrun.transaction.TransactionInfo;
import so.asch.sdk.security.SecurityException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransactionUtils {
	
//	private static String transactionUrl = "http://39.107.143.215:4096";//  /peer/transactions";
	
	public static String magic = "594fe0f3";
	
	public static String postCommonTransactionMethod(int type, long fee, String secret, String secondSecret, Object[] args) {
		String returnJson = "";
		try {
			TransactionInfo info = new TransactionBuilder().buildTransaction(type, fee, args, "杞处", secret, secondSecret);
			
			ParameterMap map = new ParameterMap();
			
			map.put("transaction", info);
			

			Map mapHeader = new HashMap<>();
			mapHeader.put("magic", magic);
			mapHeader.put("Content-Type", "application/json");
			String url = AschConfig.ContractURL + "/peer/transactions";
			
			returnJson = REST.post(url, map, mapHeader, "UTF-8");
//			System.out.println("postRet = " + returnJson);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJson;
	}
	
	/**
	 * 	鏌ヨ鍚堢害鐨刢onstant鏂规硶
	 * @param methodName
	 * @param methodArgs
	 * @return
	 */
	public static String queryGameTransactionStatus(String methodName, Object[] methodArgs) {
		String getUrl = AschConfig.ContractURL + "/api/v2/contracts/" + AschConfig.gameContractName + "/constant/" + methodName;
//		String getUrl = "http://localhost:6789/api/aschnode?ss=abc";
		
		JSONArray arr = new JSONArray();
		for (int i = 0; i < methodArgs.length; i++) {
			arr.add(methodArgs[i]);
		}

		Map mapHeader = new HashMap<>();
		mapHeader.put("magic", magic);
		mapHeader.put("Content-Type", "application/json");
		
		String args = arr.toJSONString();
		String retData = "";
		try {
			retData = REST.post(getUrl, args, mapHeader, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retData;
	}
	
	/**
	 * 璋冪敤鍚戞櫤鑳藉悎绾﹁浆璐︽帴鍙�
	 * @param secret
	 * @param secondSecret
	 * @param
	 * @param
	 * @return
	 */
	public static String postGameTransactionTransfer(String secret, String secondSecret, long payXasCount) {
		int type = 602;
		long fee = 0;
		long gasLimit = 500 * 1000;
		boolean enablePayGasInXas = true;
		
		Object[] args = new Object[6];
		args[0] = gasLimit;
		args[1] = enablePayGasInXas;
		args[2] = AschConfig.gameContractName;
		args[3] = "";
		args[4] = payXasCount * 100000000;
		args[5] = "XAS";
		String returnJson = "";
		try {
			TransactionInfo info = new TransactionBuilder().buildTransaction(type, fee, args, "", secret, secondSecret);
			
			String transaction = JSONObject.toJSONString(info);
//			System.out.println("dapptranstraction = " + transaction);
			
			
			ParameterMap map = new ParameterMap();
			
			map.put("transaction", info);
			

			Map mapHeader = new HashMap<>();
			mapHeader.put("magic", magic);
			mapHeader.put("Content-Type", "application/json");
			String url = AschConfig.ContractURL + "/peer/transactions";
			
			returnJson = REST.post(url, map, mapHeader, "UTF-8");
//			System.out.println("postRet = " + returnJson);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJson;
	}
	
	@SuppressWarnings("unchecked")
	public static String postGameTransactionMethod(String secret, String secondSecret, String method, Object[] methodArgs) {
		int type = 601;
		long fee = 0;
		long gasLimit = 500 * 1000;
		boolean enablePayGasInXas = true;
		
		Object[] args = new Object[5];
		args[0] = gasLimit;
		args[1] = enablePayGasInXas;
		args[2] = AschConfig.gameContractName;
		args[3] = method;
		args[4] = methodArgs;
		String returnJson = "";
		try {
			TransactionInfo info = new TransactionBuilder().buildTransaction(type, fee, args, "", secret, secondSecret);
			
			String transaction = JSONObject.toJSONString(info);
//			System.out.println("dapptranstraction = " + transaction);
			
			
			ParameterMap map = new ParameterMap();
			
			map.put("transaction", info);
			

			Map mapHeader = new HashMap<>();
			mapHeader.put("magic", magic);
			mapHeader.put("Content-Type", "application/json");
			String url = AschConfig.ContractURL + "/peer/transactions";
			
			returnJson = REST.post(url, map, mapHeader, "UTF-8");
//			System.out.println("postRet = " + returnJson);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJson;
	}

	@SuppressWarnings("unchecked")
	public static String postTransactionMethod(String method, Object[] methodArgs) {
		int type = 601;
		long fee = 0;
		long gasLimit = 50 * 1000;
		boolean enablePayGasInXas = false;
		
		Object[] args = new Object[5];
		args[0] = gasLimit;
		args[1] = enablePayGasInXas;
		args[2] = AschConfig.contractName;
		args[3] = method;
		args[4] = methodArgs;
		String returnJson = "";
		try {
			TransactionInfo info = new TransactionBuilder().buildTransaction(type, fee, args, "", AschConfig.secret, AschConfig.secondSecret);
			
			String transaction = JSONObject.toJSONString(info);
//			System.out.println("dapptranstraction = " + transaction);
			
			
			ParameterMap map = new ParameterMap();
			
			map.put("transaction", info);
			

			Map mapHeader = new HashMap<>();
			mapHeader.put("magic", magic);
			mapHeader.put("Content-Type", "application/json");
			String url = AschConfig.ContractURL + "/peer/transactions";
			
			returnJson = REST.post(url, map, mapHeader, "UTF-8");
//			System.out.println("postRet = " + returnJson);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJson;
	}
	
	/**
	 * 	鏌ヨ鍚堢害鐨刢onstant鏂规硶
	 * @param methodName
	 * @param methodArgs
	 * @return
	 */
	public static String queryTransactionStatus(String methodName, Object[] methodArgs) {
		String getUrl = AschConfig.ContractURL + "/api/v2/contracts/" + AschConfig.contractName + "/constant/" + methodName;
//		String getUrl = "http://localhost:6789/api/aschnode?ss=abc";
		
		JSONArray arr = new JSONArray();
		for (int i = 0; i < methodArgs.length; i++) {
			arr.add(methodArgs[i]);
		}

		Map mapHeader = new HashMap<>();
		mapHeader.put("magic", magic);
		mapHeader.put("Content-Type", "application/json");
		
		String args = arr.toJSONString();
		String retData = "";
		try {
			retData = REST.post(getUrl, args, mapHeader, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retData;
	}
}
