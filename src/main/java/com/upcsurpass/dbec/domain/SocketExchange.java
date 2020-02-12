package com.upcsurpass.dbec.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.service.method.GetServerCurrentTime;
import com.upcsurpass.dbec.tools.ByteTools;

public class SocketExchange {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketExchange.class);

	int nExchangeID; // 命令ID
	int nCount; // 数据包个数
	public int getnExchangeID() {
		return nExchangeID;
	}

	public int getnCount() {
		return nCount;
	}

	public int getnLength() {
		return nLength;
	}

	public int getnParam1() {
		return nParam1;
	}

	public int getnParam2() {
		return nParam2;
	}

	public void setnExchangeID(int nExchangeID) {
		this.nExchangeID = nExchangeID;
	}

	public void setnCount(int nCount) {
		this.nCount = nCount;
	}

	public void setnLength(int nLength) {
		this.nLength = nLength;
	}

	public void setnParam1(int nParam1) {
		this.nParam1 = nParam1;
	}

	public void setnParam2(int nParam2) {
		this.nParam2 = nParam2;
	}

	int nLength; // 本数据包长度(不含包头)
	int nParam1; // 扩展参数1
	int nParam2; // 扩展参数2
	byte buffer[] ; // 包体

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}


	public SocketExchange(byte[] b) {
//		0852000001000000040000000000000000000000199a365e
//		LOGGER.debug("新建交换区，解析开始...");
		// 写第一个参数
		nExchangeID = b[0] & 0xFF |  
                (b[1] & 0xFF) << 8 |  
                (b[2] & 0xFF) << 16 |  
                (b[3] & 0xFF) << 24;  
		
		nCount = b[4] & 0xFF |  
                (b[5] & 0xFF) << 8 |  
                (b[6] & 0xFF) << 16 |  
                (b[7] & 0xFF) << 24;  
		
		nLength = b[8] & 0xFF |  
                (b[9] & 0xFF) << 8 |  
                (b[10] & 0xFF) << 16 |  
                (b[11] & 0xFF) << 24;  
		
		nParam1 = b[12] & 0xFF |  
                (b[13] & 0xFF) << 8 |  
                (b[14] & 0xFF) << 16 |  
                (b[15] & 0xFF) << 24;  
		
		nParam2 = b[16] & 0xFF |  
                (b[17] & 0xFF) << 8 |  
                (b[18] & 0xFF) << 16 |  
                (b[19] & 0xFF) << 24;  
		byte[] r = new byte[b.length-20];
//		LOGGER.debug("新建交换区，复制数据区， 开始...");
		System.arraycopy(b, 20, r, 0, b.length-20);
//		LOGGER.debug("新建交换区，复制数据区， 结束...");
		this.buffer = r; // 这里没有取部分，是为了节省分配。
//		LOGGER.debug("新建交换区，赋值指针， 结束返回");
		
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
		LOGGER.info(this.nExchangeID+" "+this.nCount+" "+this.nLength+" "+this.nParam1+" "+this.nParam2+" "+this.buffer[0]+" "+this.buffer[1]+" "+this.buffer[2]+" "+this.buffer[3]);
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
