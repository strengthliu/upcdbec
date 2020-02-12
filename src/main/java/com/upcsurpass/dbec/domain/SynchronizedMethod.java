package com.upcsurpass.dbec.domain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.client.DBEClient;
import com.upcsurpass.dbec.client.DBEConnection;
import com.upcsurpass.dbec.client.NioSocketClient;
import com.upcsurpass.dbec.client.PooledNIOSocketClient;

/**
 * 
 * 同步方法。
 * 一个客户端可同时执行的方法，由
 * @author qiang.liu
 *
 */
public abstract class SynchronizedMethod{ // extends MethodHandle {
//	SynchronizedMethod(MethodType type, LambdaForm form) {
//		super(type, form);
//	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedMethod.class);
	protected CountDownLatch countDownLatchReqResp = new CountDownLatch(1);
//	private CountDownLatch countDownLatchDataPackageCount;// = new CountDownLatch(1);
	
	PooledNIOSocketClient pnsc;
	protected SynchronizedMethod(PooledNIOSocketClient pnsc) {
		this.pnsc = pnsc;
		this.se = setSocketExchange();
		registe();
	}

	protected SynchronizedMethod(){}
	protected SocketExchange se;
	public abstract SocketExchange setSocketExchange();
	
	/**
	 * 注册一个本方法到一个客户端上，以接收客户端的响应。
	 * @param nsc
	 */
	public void registe() {
//		this.pnsc = pnsc;
		pnsc.addRequestBind(this);
	}
	/**
	 * 返回数据
	 * 该如何处理？如何与nsc通信
	 */
	protected ArrayList<byte[]> response = new ArrayList<byte[]>();
	protected transient int packageCount = 0;
	/**
	 * 添加返回值，由客户端调用。
	 * 每一包解析，当返回值完整，就结束挂起。
	 */
//	public abstract void addResponseData(SocketExchange se);
	public void addResponseData(SocketExchange se ) {
		LOGGER.debug("addResponseData .. 开始 .. getnExchangeID="+se.getnExchangeID()+" this.se.nExchangeID="+this.se.nExchangeID);
		if(se.getnExchangeID()==this.se.nExchangeID*GlobalConsts.RESPONSE_COM_RATE) {
			//如果是第1包，要判断包数
		    if(this.response==null||this.response.size()==0) {
		    	LOGGER.debug("addResponseData .. 第1包数据打包， ..共有"+se.getnCount()+" 包数据。");
				this.packageCount = se.getnCount();
		    }
			LOGGER.debug("addResponseData .. 添加一包byte数据 ..");
			LOGGER.debug(".....................................................................");
		    this.response.add(se.getBuffer());
			LOGGER.debug("addResponseData .. 添加一包byte数据 结束 ..");
			LOGGER.debug("addResponseData .. countDown 数据传输结束，通知打包返回 ..");
//		    System.arraycopy(se.getBuffer(), 0, this.response, this.response.size(), 1);
		    if(this.packageCount == this.response.size()) {
				LOGGER.debug("addResponseData .. countDown 数据传输结束，通知打包返回 ..");
		    	this.countDownLatchReqResp.countDown();
		    }
		}
		LOGGER.debug("addResponseData .. 结束 ..");

//	    System.arraycopy(d, 0, this.response, this.response.length, 1);
	}

	public Object doRequest() {
		// 设置锁
//		nsc.setCountDownLatchReqResp(countDownLatchReqResp);
		// 调用客户端发请求
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LOGGER.debug("=============准备发送=========================");
		pnsc.sendData(se.toByte());
		LOGGER.debug("发送完成=========================");
		try {
			countDownLatchReqResp.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 如果
		// 返回后
//		pnsc.r
		return parseObject(response);
	}
	
	
	public abstract Object parseObject(ArrayList<byte[]> d);
	
}
