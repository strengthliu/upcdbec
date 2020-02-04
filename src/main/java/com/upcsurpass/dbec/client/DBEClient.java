package com.upcsurpass.dbec.client;

import java.util.Hashtable;

import com.upcsurpass.dbec.appCfg.GlobalConsts;

/**
 * 参考一下JDBC。
 * 支持多库
 * @author qiang.liu
 *
 */
public class DBEClient implements IDBEClient {

	
	static Hashtable<String,Integer> servers = new Hashtable<String,Integer>();
	
	static Hashtable<String,DBEConnection> connections = new Hashtable<String,DBEConnection>();
	
	private static DBEClient client = new DBEClient();
	private DBEClient() {
//		servers = new Hashtable<String,Integer>();
//		connections = new Hashtable<String,DBEConnection<NioSocketClient>>();
	}
	
	public void connect(String serverName, int port) {
		servers.put(serverName, port);
		DBEConnection connection = new DBEConnection(serverName,port);
		connections.put(serverName, connection);
	}

	public void close(String serverName) {
		servers.remove(serverName);
		DBEConnection connection = connections.get(serverName);
		connection.closeObjectPool();
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
