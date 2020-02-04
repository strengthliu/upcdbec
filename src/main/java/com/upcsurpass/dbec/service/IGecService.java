package com.upcsurpass.dbec.service;

import java.nio.ByteBuffer;
import java.util.List;

import com.upcsurpass.dbec.exception.GecException;

public interface IGecService {

	public long DBECGetLastError();

	public String DBECGetErrorMessage(long nErrorCode) throws GecException;

	public void DBEC_ExitInstance() throws GecException;

	public long DBECGetServerCurrentTime(String lpszServerName);

	public void DBECSetLocalServerTime(String lpszServerName) throws GecException;

	public long DBECGetDeviceID(String lpszServerName, String lpszDeviceName) throws GecException;

	public String DBECGetDeviceName(String lpszServerName, long nDeviceID) throws GecException;

	public long DBECGetTagID(String lpszServerName, String lpszTagName) throws GecException;

	public long DBECGetFieldID(String lpszFieldName) throws GecException;

	public long DBECGetServerCount();

	public long DBECGetTagMaxCount(String lpszServerName);

	public long DBECGetClientMaxCount(String lpszServerName);

	public long DBECGetClientCount(String lpszServerName);

	public long DBECGetDeviceCount(String lpszServerName);

	public long DBECGetTagCount(String lpszServerName);

	public long DBECGetTagCountOfDevice(String lpszServerName, long nDeviceID);

	public long DBECGetTagCountOfDeviceByDeviceName(String lpszServerName, String lpszDeviceName);

	public long DBECGetServerNameMaxLen();

	public long DBECGetClientNameMaxLen();

	public long DBECGetDeviceNameMaxLen();

	public long DBECGetTagNameMaxLen();

	public long DBECGetFieldNameMaxLen();

	public long DBECGetFieldStringValueMaxLen();

	public long DBECGetErrMsgMaxLen();

	public long DBECGetEventNameMaxLen();

	public long DBECGetEventConditionMaxLen();

	public long DBECGetTagType(String lpszServerName, long nTagID);

	public long DBECGetTagTypeByTagName(String lpszServerName, String lpszTagName);

	public String DBECGetTagName(String serverName, long tagId) throws GecException;

	public long DBECGetFieldType(String lpszFieldName) throws GecException;

	public List<String> DBECEnumServerName() throws GecException;

	public List<String> DBECEnumDeviceName(String lpszServerName) throws GecException;

	public double DBECGetTagRealField(String lpszServerName, long nTagID, String lpszFieldName) throws GecException;

	public double DBECGetTagRealFieldByTagName(String lpszServerName, String lpszTagName, String lpszFieldName)
			throws GecException;

	public List<Double> DBECBatchGetTagRealField(String lpszServerName, List<Long> pnIDArray, String lpszFieldName)
			throws GecException;

	public List<String> DBECEnumTagExtendType(String lpszServerName) throws GecException;

	public List<Long> DBECEnumTagID(String lpszServerName) throws GecException;

	// test.DBECEnumTagID();
	public List<Long> DBECEnumTagIDOfDevice(String lpszServerName, long nDeviceID) throws GecException;

	public List<Long> DBECEnumTagIDOfDeviceByDeviceName(String lpszServerName, String lpszDeviceName)
			throws GecException;

	public void DBECGetTagRealHistory(String lpszServerName, String lpszTagName, long nTagID, long nBeginTime,
			long nEndTime, List<Double> pValueArray, int nArraySize, List<Long> pnValueTimeArray) throws GecException;

	public long DBECGetTagIntField(String lpszServerName, String lpszTagName, long nTagID, String lpszFieldName)
			throws GecException;

	public String DBECGetTagStringField(String lpszServerName, String lpszTagName, long nTagID, String lpszFieldName)
			throws GecException;

	public void DBACGetCurrentAlarm(String lpszServerName, List<Long> pnTagIDArray, List<Long> pAlarmTypeArray,
			int nArraySize, List<Double> pValueArray, List<Long> pOccuredTimeArray) throws GecException;

	public void DBACGetHistoryAlarm(String lpszServerName, List<Long> pnTagIDArray, long nBeginTime, long nEndTime,
			List<Long> pnAlarmTagIDArray, int nArraySize, List<Long> pnAlarmCount, List<Long> pAlarmBeginTimeArray,
			List<Long> pAlarmEndTimeArray, List<Long> pAlarmTypeArray) throws GecException;

	public String DBECGetDeviceNote(String lpszServerName, String lpszDeviceName, long nDeviceID, int nBufLen)
			throws GecException;

	public String DBECGetTagStringFields(String serverName, String tagName, Long pointId, ByteBuffer tagbuffer,
			String string);

	public String DBECGetDeviceNote(String serverName, String deviceName, Long deviceId);
}
