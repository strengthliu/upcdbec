package com.upcsurpass.dbec;

import java.util.Date;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.DBEClient;
import com.upcsurpass.dbec.exception.GecException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		// GetServerCurrentTime gct = new GetServerCurrentTime();
//		DBEClient.connect("192.168.1.149", GlobalConsts.DEFAULT_DBESERVERPORT);
//		long t = DBEClient.DBECGetServerCurrentTime("192.168.1.149");
		
		DBEClient.connect("192.168.1.149", GlobalConsts.DEFAULT_DBESERVERPORT);
		long t = DBEClient.DBECGetServerCurrentTime("192.168.1.149");
		System.out.println("========>  收到时间： "+new Date(t*1000).toLocaleString());
		System.out.println("========================================================");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long s = DBEClient.DBECGetServerCurrentTime("192.168.1.149");
//		try {
//			DBEClient.DBECServerInfo("192.168.43.127");
//		} catch (GecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(new Date(t*1000).toLocaleString());
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
