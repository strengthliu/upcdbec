package com.upcsurpass.dbec.appCfg;

import java.util.List;

public final class GlobalConsts {

	public static final int  DEFAULT_DBESERVERPORT		= 51024;	//DBES端口号
	public static final int  DEFAULT_ISERVERPORT			= 51025;	//IServer端口号

	public static final int MAXLEN_EXCHANGEBUFFER	= 32768;  // 每个数据包的包体最大长度
	
	public static final int RESPONSE_COM_RATE = 1000;
	/**
	 * 9 命令ID列表
	 */
	// DBEC与DBES握手信号：（反馈信号=握手信号*1000）
	public static final int DBEC_INIT = 10000;
	public static final int DBEC_GETSERVERCURRENTTIME = 21;
	public static final int DBEC_SETLOCALSERVERTIME = 22;

	public static final int DBEC_GETDEVICEID = 31;
	public static final int DBEC_GETDEVICENAME = 32;
	public static final int DBEC_GETTAGID = 33;
	public static final int DBEC_GETTAGNAME = 34;
	public static final int DBEC_GETFIELDID = 35;
	public static final int DBEC_GETFIELDNAME = 36;

	public static final int DBEC_GETTAGMAXCOUNT = 42;
	public static final int DBEC_GETCLIENTMAXCOUNT = 43;
	public static final int DBEC_GETCLIENTCOUNT = 44;
	public static final int DBEC_GETDEVICECOUNT = 45;
	public static final int DBEC_GETTAGCOUNT = 46;
	public static final int DBEC_GETTAGCOUNTOFDEVICE = 47;

	public static final int DBEC_GETTAGTYPE = 61;
	public static final int DBEC_GETFIELDTYPE = 62;

	public static final int DBEC_GETTAGREALFIELD = 71;
	public static final int DBEC_GETTAGINTFIELD = 72;
	public static final int DBEC_GETTAGSTRINGFIELD = 73;

	public static final int DBEC_ENUMCLIENTNAME = 82;
	public static final int DBEC_ENUMDEVICENAME = 83;
	public static final int DBEC_ENUMTAGNAME = 84;
	public static final int DBEC_ENUMTAGNAMEOFDEVICE = 85;
	public static final int DBEC_ENUMTAGEXTENDTYPE = 86;

	public static final int DBEC_BATCHGETDEVICEID = 91;
	public static final int DBEC_BATCHGETDEVICENAME = 92;
	public static final int DBEC_BATCHGETTAGID = 93;
	public static final int DBEC_BATCHGETTAGNAME = 94;

	public static final int DBEC_BATCHGETTAGREALFIELD = 101;
	public static final int DBEC_BATCHGETTAGINTFIELD = 102;
	public static final int DBEC_BATCHGETTAGSTRINGFIELD = 103;

	public static final int DBEC_GETTAGREALHISTORY = 111;
	public static final int DBEC_GETTAGINTHISTORY = 112;
	public static final int DBEC_GETTAGSTRINGHISTORY = 113;

	// DBAC与DBAS握手信号：（反馈信号=握手信号*1000）
	public static final int DBAC_GETCURRENTALARM = 121;
	public static final int DBAC_GETHISTORYALARM = 122;

	public static final int DBAC_ENUMEVENTRECORDNAME = 131;
	public static final int DBAC_ENUMEVENTNAME = 133;
	public static final int DBAC_ENUMEVENTTAGNAME = 134;
	public static final int DBAC_GETEVENTCONDITION = 135;
	public static final int DBAC_GETEVENTNAME = 136;
	public static final int DBAC_GETEVENTRECORDTIME = 137;

	// 写操作握手信号：
	public static final int DBEC_BATCHWRITETAGDATA = 141;
	public static final int DBEC_WRITEHISTORY = 142;

	public static final int DBEC_BATCHSETTAGREALFIELD = 146;
	public static final int DBEC_BATCHSETTAGINTFIELD = 147;
	public static final int DBEC_BATCHSETTAGSTRINGFIELD = 148;

	// CIMIO Server与Client握手信号：（反馈信号=握手信号*1000）
	public static final int SOCKID_GETDEVICECONFIG = 1;
	public static final int SOCKID_UPDATEDEVICERTDATA = 2;

	// DBEC扩展函数
	public static final int DBEC_GETTAGREALHISTORYEX = 161;
	public static final int DBEC_GETTAGCUMULATE = 163;
	public static final int DBEC_GETDEVICENOTE = 165;

	public static final int DBAC_GETHISTORYALARMEX = 171;

	// 扩展：
	public static final int DBEC_INITDATABASE = 10000;
	public static final int DBES_UPDATEDATABASE = 10001;
	/******************************************************************************/

	// 10 字段ID定义列表
	/******************************************************************************/
	// 基本字段：
	public static final int FID_TAGID = 0x0001; // 位号ID，整数型
	public static final int FID_TAGNAME = 0x0002;// 位号名称，字符串
	public static final int FID_TAGALIAS = 0x0003;// 原始设备位号名称，字符串
	public static final int FID_TAGNOTE = 0x0004; // 位号说明，字符串
	public static final int FID_TAGTYPE = 0x0005; // 位号类型，整数型
	public static final int FID_TAGCLASS = 0x0006;// 位号类别，字符串

	public static final int FID_INPUT_VALUE = 0x0007;// 实时数据值，字节指针
	public static final int FID_INPUT_DATATYPE = 0x0008;// 实时数据类型，整数型
	public static final int FID_INPUT_DATASIZE = 0x0009;// 实时数据长度，整数型
	public static final int FID_INPUT_DATATIME = 0x000A;// 实时数据时间，整数型
	public static final int FID_INPUT_STATE = 0x000B; // 实时数据状态，整数型

	public static final int FID_SP = 0x000C; // 模拟量SP值，浮点型
	public static final int FID_OP = 0x000D; // 模拟量OP值，浮点型
	public static final int FID_MINIMUM = 0x000E; // 量程下限，浮点型
	public static final int FID_MAXIMUM = 0x000F; // 量程上限，浮点型
	public static final int FID_ALARM_LOLO = 0x0010; // 低低报警，浮点型
	public static final int FID_ALARM_LOW = 0x0011; // 低报警，浮点型
	public static final int FID_ALARM_HIGH = 0x0012; // 高报警，浮点型
	public static final int FID_ALARM_HIHI = 0x0013; // 高高报警，浮点型
	public static final int FID_ALARM_DEADBAND = 0x0014; // 报警死区，浮点型
	public static final int FID_ALARM_SWITCH = 0x0015;// 报警开关，整数型
	public static final int FID_ALARM_MODE = 0x0016; // 报警模式，整数型
	public static final int FID_ALARM_STATE = 0x0017; // 报警状态，整数型

	public static final int FID_GROUPID = 0x0018; // 组ID，整数型
	public static final int FID_GROUPNAME = 0x0019; // 组名称，字符串
	public static final int FID_TABLEID = 0x001A;// 表ID，整数型
	public static final int FID_TABLENAME = 0x001B; // 表名称，字符串

	public static final int FID_UNIT = 0x001C; // 实时数据单位，字符串
	public static final int FID_FORMAT = 0x001D; // 实时数据显示格式，字符串

	public static final int FID_ALARM_OCCURTIME = 0x001E; // 报警发生时间
	/******************************************************************************/

}
