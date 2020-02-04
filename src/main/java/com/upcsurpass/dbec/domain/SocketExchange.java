package com.upcsurpass.dbec.domain;

import com.upcsurpass.dbec.tools.ByteTools;

public class SocketExchange {

	int nExchangeID; // 命令ID
	int nCount; // 数据包个数
	int nLength; // 本数据包长度(不含包头)
	int nParam1; // 扩展参数1
	int nParam2; // 扩展参数2
	byte buffer[]; // 包体

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}


	public SocketExchange(Byte[] data) {
//		0852000001000000040000000000000000000000199a365e
		
	}


	public SocketExchange(int nExchangeID,int nCount,int nLength,int nParam1,int nParam2) {
		this.nExchangeID = nExchangeID;
		this.nCount = nCount;
		this.nLength = nLength;
		this.nParam1 = nParam1;
		this.nParam2 = nParam2;
	}

	/**
	 * 
	 * // (4Bytes) 命令ID // (4Bytes) 数据包个数/序号 // (4Bytes) 数据包长度 // (4Bytes) 扩展参数1 //
	 * (4Bytes) 扩展参数2 // 包体(可变长度)(最大32768Bytes)
	 * 
	 * @return
	 */
	public byte[] toByte() {
		int bufferLength = 0;
		if(buffer!=null) bufferLength = buffer.length;
		int length = 20 + bufferLength;
		byte[] ret = new byte[length];
		// 写第一个参数
		// byte[] bnExchangeID = ByteTools.intToByteArray(nExchangeID);
		ret[0] = (byte) (nExchangeID & 0xFF);
		ret[1] = (byte) ((nExchangeID >> 8) & 0xFF);
		ret[2] = (byte) ((nExchangeID >> 16) & 0xFF);
		ret[3] = (byte) ((nExchangeID >> 24) & 0xFF);

		ret[4] = (byte) (nCount & 0xFF);
		ret[5] = (byte) ((nCount >> 8) & 0xFF);
		ret[6] = (byte) ((nCount >> 16) & 0xFF);
		ret[7] = (byte) ((nCount >> 24) & 0xFF);

		ret[8] = (byte) (nLength & 0xFF);
		ret[9] = (byte) ((nLength >> 8) & 0xFF);
		ret[10] = (byte) ((nLength >> 16) & 0xFF);
		ret[11] = (byte) ((nLength >> 24) & 0xFF);

		ret[12] = (byte) (nParam1 & 0xFF);
		ret[13] = (byte) ((nParam1 >> 8) & 0xFF);
		ret[14] = (byte) ((nParam1 >> 16) & 0xFF);
		ret[15] = (byte) ((nParam1 >> 24) & 0xFF);

		ret[16] = (byte) (nParam2 & 0xFF);
		ret[17] = (byte) ((nParam2 >> 8) & 0xFF);
		ret[18] = (byte) ((nParam2 >> 16) & 0xFF);
		ret[19] = (byte) ((nParam2 >> 24) & 0xFF);

		for(int i=20,j=0;j<bufferLength;i++,j++) {
			ret[i] = buffer[j];
		}
		return ret;
	}
}
