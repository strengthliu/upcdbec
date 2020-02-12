package com.upcsurpass.dbec.client;

import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.exception.GecException;
import com.upcsurpass.dbec.service.IGecService;
import com.upcsurpass.dbec.service.method.GetServerCurrentTime;
import com.upcsurpass.dbec.service.method.GetServerDevicePointInitInfo;

/**
 * 参考一下JDBC。
 * 支持多库
 * @author qiang.liu
 *
 */
public class DBEClient implements IDBEClient{//,IGecService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBEClient.class);
//	static Hashtable<String,Integer> servers = new Hashtable<String,Integer>();
	
	static Hashtable<String,DBEConnection> connections = new Hashtable<String,DBEConnection>();
	
	private static DBEClient client = new DBEClient();
	private DBEClient() {}

	/********************* IDBEClient ***********************/
	
	public static void connect(String serverName, int port) {
		LOGGER.debug("测试连接参数有效性开始： serverName="+serverName+" port="+port);
//		servers.put(serverName, port);
		// 测试连接是否正确
		boolean connectable = SocketClient.testConnect(serverName,port);
		if(connectable) {
			DBEConnection connection = new DBEConnection(serverName,port);
			connections.put(serverName, connection);
		}else {
			throw new IllegalStateException("指定的GIP服务器地址和端口，不能正常连接。");
		}
	}

	public static void close(String serverName) {
//		servers.remove(serverName);
		DBEConnection connection = connections.get(serverName);
		connection.closeObjectPool();
		
	}

	public static void close() {
		Enumeration<String> e = connections.keys();
		while(e.hasMoreElements()) {
			String k = e.nextElement();
			close(k);
		}
	}
	
	/********************* IGecService ***********************/
	/**
	 * 不同请求，发送数据不同，接收数据不同，处理不同。
	 * 同服务器连接相同。
	 */

	/**
	 * 从服务器上获取服务器信息，包括服务器、设备、点位等。
	 * 信息量很大，获取后要保存在本地缓存。后面读取的都是从本地读取。
	 * 如果是UPCVision,已经做了保存机制，就不需要保存了。
	 * 
	 * @param lpszServerName
	 * @throws GecException
	 */
	public static void DBECServerInfo(String lpszServerName) throws GecException{
		DBEConnection connection = connections.get(lpszServerName);
		PooledNIOSocketClient pnsc = connection.getObject();
		LOGGER.debug(" 开始执行请求 ： PooledNIOSocketClient=> "+pnsc.toString());
		
		//========================================================
		GetServerDevicePointInitInfo gsct = new GetServerDevicePointInitInfo(pnsc);
		Object l =  gsct.doRequest();
		//========================================================
		LOGGER.debug(l.toString());
		connection.returnObject(pnsc);
		return;
		
	}
	
	/**
	 * TODO: 这里有个要确认的东西。
	 * 		这里的服务器是指DBES，还是用户的实时数据库？
	 * @return
	 * @throws GecException
	 */
	public static List<String> DBECEnumServerName() throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public static List<String> DBECEnumDeviceName(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public static List<Long> DBECEnumTagID(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public static List<Long> DBECEnumTagIDOfDevice(String lpszServerName, long nDeviceID) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public static List<Long> DBECEnumTagIDOfDeviceByDeviceName(String lpszServerName, String lpszDeviceName)
			throws GecException {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 获取当前服务器时间
	 * @param lpszServerName
	 * @return
	 */
	public static long DBECGetServerCurrentTime(String lpszServerName) {
		DBEConnection connection = connections.get(lpszServerName);
		PooledNIOSocketClient pnsc = connection.getObject();
		LOGGER.debug(" 开始执行请求 ： PooledNIOSocketClient=> "+pnsc.toString());
		
		//========================================================
		GetServerCurrentTime gsct = new GetServerCurrentTime(pnsc);
		Long l = (Long) gsct.doRequest();
		//========================================================
		
		connection.returnObject(pnsc);
		return l;
	}

	
	/**
	 * 设置本地服务器时间
	 * @param lpszServerName
	 * @throws GecException
	 */
	public void DBECSetLocalServerTime(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		
	}


	
	public long DBECGetLastError() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String DBECGetErrorMessage(long nErrorCode) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void DBEC_ExitInstance() throws GecException {
		// TODO Auto-generated method stub
		
	}

	
	public long DBECGetDeviceID(String lpszServerName, String lpszDeviceName) throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String DBECGetDeviceName(String lpszServerName, long nDeviceID) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long DBECGetTagID(String lpszServerName, String lpszTagName) throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetFieldID(String lpszFieldName) throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetServerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagMaxCount(String lpszServerName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetClientMaxCount(String lpszServerName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetClientCount(String lpszServerName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetDeviceCount(String lpszServerName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagCount(String lpszServerName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagCountOfDevice(String lpszServerName, long nDeviceID) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagCountOfDeviceByDeviceName(String lpszServerName, String lpszDeviceName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetServerNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetClientNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetDeviceNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetFieldNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetFieldStringValueMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetErrMsgMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetEventNameMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetEventConditionMaxLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagType(String lpszServerName, long nTagID) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long DBECGetTagTypeByTagName(String lpszServerName, String lpszTagName) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String DBECGetTagName(String serverName, long tagId) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long DBECGetFieldType(String lpszFieldName) throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double DBECGetTagRealField(String lpszServerName, long nTagID, String lpszFieldName) throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double DBECGetTagRealFieldByTagName(String lpszServerName, String lpszTagName, String lpszFieldName)
			throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Double> DBECBatchGetTagRealField(String lpszServerName, List<Long> pnIDArray, String lpszFieldName)
			throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<String> DBECEnumTagExtendType(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void DBECGetTagRealHistory(String lpszServerName, String lpszTagName, long nTagID, long nBeginTime,
			long nEndTime, List<Double> pValueArray, int nArraySize, List<Long> pnValueTimeArray) throws GecException {
		// TODO Auto-generated method stub
		
	}

	
	public long DBECGetTagIntField(String lpszServerName, String lpszTagName, long nTagID, String lpszFieldName)
			throws GecException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String DBECGetTagStringField(String lpszServerName, String lpszTagName, long nTagID, String lpszFieldName)
			throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void DBACGetCurrentAlarm(String lpszServerName, List<Long> pnTagIDArray, List<Long> pAlarmTypeArray,
			int nArraySize, List<Double> pValueArray, List<Long> pOccuredTimeArray) throws GecException {
		// TODO Auto-generated method stub
		
	}

	
	public void DBACGetHistoryAlarm(String lpszServerName, List<Long> pnTagIDArray, long nBeginTime, long nEndTime,
			List<Long> pnAlarmTagIDArray, int nArraySize, List<Long> pnAlarmCount, List<Long> pAlarmBeginTimeArray,
			List<Long> pAlarmEndTimeArray, List<Long> pAlarmTypeArray) throws GecException {
		// TODO Auto-generated method stub
		
	}

	
	
	public String DBECGetTagStringFields(String serverName, String tagName, Long pointId, ByteBuffer tagbuffer,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public String DBECGetDeviceNote(String lpszServerName, String lpszDeviceName, long nDeviceID, int nBufLen)
			throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String DBECGetDeviceNote(String serverName, String deviceName, Long deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
