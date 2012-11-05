package com.rik.shared;


import java.net.URL;

import org.json.rpc.client.HttpJsonRpcClientTransport;
import org.json.rpc.client.JsonRpcInvoker;



public class CRPC {
	
	//static String serverUrl = "http://reddit-imaginary-karma.appspot.com/jsonrpc";
	static String serverUrl = "http://10.0.2.2:8888/jsonrpc";
public static Rpc getRPC() {
		return getRPC(serverUrl);
	}

	public static Rpc getRPC(String url)  {
		Rpc rpc = null;
		try {
			HttpJsonRpcClientTransport transport = null;
			transport = new HttpJsonRpcClientTransport(new URL(url));
			JsonRpcInvoker invoker = new JsonRpcInvoker();

			rpc = invoker.get(transport, "rpc", Rpc.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading from server.");
		}
		return rpc;
	}

}
