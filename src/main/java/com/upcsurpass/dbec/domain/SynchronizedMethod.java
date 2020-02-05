package com.upcsurpass.dbec.domain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
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
	public SynchronizedMethod(PooledNIOSocketClient pnsc) {
		this.pnsc = pnsc;
		this.se = setSocketExchange();
		registe();
	}

	private SocketExchange se;
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
		if(se.getnExchangeID()==GlobalConsts.DBEC_GETSERVERCURRENTTIME*GlobalConsts.RESPONSE_COM_RATE) {
			//如果是第1包，要判断包数
		    if(this.response==null||this.response.size()==0) {
				this.packageCount = se.getnCount();
		    }
		    this.response.add(se.getBuffer());
//		    System.arraycopy(se.getBuffer(), 0, this.response, this.response.size(), 1);
		    if(this.packageCount == this.response.size())
		    		this.countDownLatchReqResp.countDown();
		}
			
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
