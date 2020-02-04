package com.upcsurpass.dbec.client;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.NioSocketClient;

/**
 * 一个connection保持对一个数据库的连接池。
 * 
 * @author qiang.liu
 *
 * @param <NioSocketClient>
 */
public class DBEConnection extends DBECConnectionPool {

	String serverName;
	String serverIP;
	int port;

	public DBEConnection(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;
		this.createPool();
	}

	@Override
	public PooledNIOSocketClient create() {
		PooledNIOSocketClient ins = new PooledNIOSocketClient(serverName, GlobalConsts.DEFAULT_DBESERVERPORT);
		ins.start();
		return ins;
	}

}
