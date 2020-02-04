package com.upcsurpass.dbec.service.method;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.NioSocketClient;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.domain.SynchronizedMethod;

public class GetServerCurrentTime extends SynchronizedMethod {

	public GetServerCurrentTime(String serverName) {
      NioSocketClient nsc = NioSocketClient.create(serverName, GlobalConsts.DEFAULT_DBESERVERPORT);
//      DBEClient
//      nsc.start();
      this.registe(nsc);
      byte[] data = new byte[4];
      data[0] = 'N';
      data[1] = 'U';
      data[2] = 'L';
      data[3] = 'L';
//      21	1	4	0	0	NULL
      SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME,1,4,0,0);
      se.setBuffer(data);
	}
	@Override
	public Object parseObject(byte[] d) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
