package com.upcsurpass.dbec.client;

import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.exception.GecException;
import com.upcsurpass.dbec.service.IGecService;
import com.upcsurpass.dbec.service.method.GetServerCurrentTime;

/**
 * 参考一下JDBC。
 * 支持多库
 * @author qiang.liu
 *
 */
public class DBEClient implements IDBEClient{//,IGecService {
//	static Hashtable<String,Integer> servers = new Hashtable<String,Integer>();
	
	static Hashtable<String,DBEConnection> connections = new Hashtable<String,DBEConnection>();
	
	private static DBEClient client = new DBEClient();
	private DBEClient() {}

	/********************* IDBEClient ***********************/
	
	public static void connect(String serverName, int port) {
//		servers.put(serverName, port);
		DBEConnection connection = new DBEConnection(serverName,port);
		connections.put(serverName, connection);
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
	
	public static long DBECGetServerCurrentTime(String lpszServerName) {
		DBEConnection connection = connections.get(lpszServerName);
		PooledNIOSocketClient pnsc = connection.getObject();
		
		//========================================================
		GetServerCurrentTime gsct = new GetServerCurrentTime(pnsc);
		Long l = (Long) gsct.doRequest();
		//========================================================
		
		connection.returnObject(pnsc);
		return l;
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

	
	public void DBECSetLocalServerTime(String lpszServerName) throws GecException {
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

	
	public List<String> DBECEnumServerName() throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<String> DBECEnumDeviceName(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		return null;
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

	
	public List<Long> DBECEnumTagID(String lpszServerName) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Long> DBECEnumTagIDOfDevice(String lpszServerName, long nDeviceID) throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Long> DBECEnumTagIDOfDeviceByDeviceName(String lpszServerName, String lpszDeviceName)
			throws GecException {
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

	
	public String DBECGetDeviceNote(String lpszServerName, String lpszDeviceName, long nDeviceID, int nBufLen)
			throws GecException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String DBECGetTagStringFields(String serverName, String tagName, Long pointId, ByteBuffer tagbuffer,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String DBECGetDeviceNote(String serverName, String deviceName, Long deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
