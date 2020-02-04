package com.upcsurpass.dbec.service.method;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.NioSocketClient;
import com.upcsurpass.dbec.client.PooledNIOSocketClient;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.domain.SynchronizedMethod;

public class GetServerCurrentTime extends SynchronizedMethod {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetServerCurrentTime.class);

	/**
	 * 这里应该修改一下，自动处理，而不应该留给用户
	 * @param pnsc
	 */
	public GetServerCurrentTime(PooledNIOSocketClient pnsc) {
		super(pnsc);
	}


	@Override
	public SocketExchange setSocketExchange() {
		// 21 1 4 0 0 NULL
		byte[] data = { 'N', 'U', 'L', 'L' };
		SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME, 1, 4, 0, 0);
		se.setBuffer(data);
		return se;
	}


	@Override
	public Object parseObject(ArrayList<byte[]> d) {
		LOGGER.info("d=>"+d.size());
		// TODO Auto-generated method stub
//		int len = 80;
//      ByteBuffer buffer = ByteBuffer.allocate(len);
//      boolean result = gec.DBECGetDeviceNote(serverName, deviceName, new NativeLong(deviceId), buffer, new NativeLong(len));
//		byte[] sb = buffer.array();
//		String deviceNote = null;
//		try {
//			deviceNote = new String(sb, "GBK");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
////		return deviceNote = deviceNote.trim();
//		return deviceNote.trim();
//	}

		return null;
	}

}
