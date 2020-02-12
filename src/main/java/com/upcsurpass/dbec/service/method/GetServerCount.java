package com.upcsurpass.dbec.service.method;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.NioSocketClient;
import com.upcsurpass.dbec.client.PooledNIOSocketClient;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.domain.SynchronizedMethod;
import com.upcsurpass.dbec.tools.ByteTools;

public class GetServerCount extends SynchronizedMethod {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetServerCount.class);

	/**
	 * 这里应该修改一下，自动处理，而不应该留给用户
	 * @param pnsc
	 */
	public GetServerCount(PooledNIOSocketClient pnsc) {
		super(pnsc);
	}


	@Override
	public SocketExchange setSocketExchange() {
		// 21 1 4 0 0 NULL
		byte[] data = { 'N', 'U', 'L', 'L' };
		SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME, 1, 4, 0, 0);
//		SocketExchange se = new SocketExchange(GlobalConsts.DBEC_ENUMTAGNAME, 1, 4, 0, 0);
		
		se.setBuffer(data);
		return se;
	}


	@Override
	public Object parseObject(ArrayList<byte[]> d) {
//		LOGGER.debug("d=>"+d.size());
		for(int i=0;i<d.size();i++) {
//			LOGGER.debug("parseObject => "+d.get(i).toString());
		}
		if(d.size()>0) {
			Long l = null;
			try {
//				LOGGER.debug(d.get(0).length+"");
				int ii = ByteTools.byteArrayToInt(d.get(0));
				return Long.valueOf(ii);
//				l = ByteTools.bytes2long(d.get(0));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			LOGGER.debug("parseObject => "+l.toString());
			return l;
		}

		return null;
	}
	// TODO 关于中文字符集的处理例子。
//	int len = 80;
//  ByteBuffer buffer = ByteBuffer.allocate(len);
//  boolean result = gec.DBECGetDeviceNote(serverName, deviceName, new NativeLong(deviceId), buffer, new NativeLong(len));
//	byte[] sb = buffer.array();
//	String deviceNote = null;
//	try {
//		deviceNote = new String(sb, "GBK");
//	} catch (UnsupportedEncodingException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
////	return deviceNote = deviceNote.trim();
//	return deviceNote.trim();
//}

}
