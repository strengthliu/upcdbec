package com.upcsurpass.dbec;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.NioSocketClient;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.domain.SynchronizedMethod;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        System.out.println( "Hello World!" );
        NioSocketClient nsc = NioSocketClient.create("192.168.1.149", GlobalConsts.DEFAULT_DBESERVERPORT);
        nsc.start();
//        NioSocketClient nsc = NioSocketClient.create("127.0.0.1", 12345);
        SynchronizedMethod sm = null ;//= new SynchronizedMethod();
        sm.registe(nsc);
        byte[] data = new byte[4];
        data[0] = 'N';
        data[1] = 'U';
        data[2] = 'L';
        data[3] = 'L';
//        21	1	4	0	0	NULL
        SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME,1,4,0,0);
        se.setBuffer(data);
        
        Object r = sm.doRequest(se);
        System.out.println("a->"+r);
////        包体(可变长度)(最大32768Bytes)
//        for(int i=0;i<2;i++) {
//            nsc.sendData(se.toByte());
//            try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//        }
//        nsc.shutDown();
        
        
    }
}
