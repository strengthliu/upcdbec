package com.upcsurpass.dbec;

import java.util.Date;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.DBEClient;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		// GetServerCurrentTime gct = new GetServerCurrentTime();
//		DBEClient.connect("192.168.1.149", GlobalConsts.DEFAULT_DBESERVERPORT);
//		long t = DBEClient.DBECGetServerCurrentTime("192.168.1.149");
		
		DBEClient.connect("192.168.43.127", GlobalConsts.DEFAULT_DBESERVERPORT);
		long t = DBEClient.DBECGetServerCurrentTime("192.168.43.127");
		
		System.out.println(new Date(t).toLocaleString());
		DBEClient.close();
	}

	public static void test1() {
		// System.out.println( "Hello World!" );
//		NioSocketClient nsc = NioSocketClient.create("192.168.1.149", GlobalConsts.DEFAULT_DBESERVERPORT);
//		nsc.start();
		// NioSocketClient nsc = NioSocketClient.create("127.0.0.1", 12345);
		// SynchronizedMethod sm = new SynchronizedMethod(nsc);
		// sm.registe(nsc);
		// byte[] data = new byte[4];
		// data[0] = 'N';
		// data[1] = 'U';
		// data[2] = 'L';
		// data[3] = 'L';
		//// 21 1 4 0 0 NULL
		// SocketExchange se = new
		// SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME,1,4,0,0);
		// se.setBuffer(data);
		//
		// Object r = sm.doRequest(se);
		// System.out.println("a->"+r);

		//// 包体(可变长度)(最大32768Bytes)
		// for(int i=0;i<2;i++) {
		// nsc.sendData(se.toByte());
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// nsc.shutDown();

	}
}
