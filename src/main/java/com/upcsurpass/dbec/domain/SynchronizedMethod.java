package com.upcsurpass.dbec.domain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.concurrent.CountDownLatch;

import com.upcsurpass.dbec.client.NioSocketClient;

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
	
	private CountDownLatch countDownLatchReqResp = new CountDownLatch(1);
	private CountDownLatch countDownLatchDataPackageCount;// = new CountDownLatch(1);
	
	NioSocketClient nsc;
	/**
	 * 注册一个本方法到一个客户端上，以接收客户端的响应。
	 * @param nsc
	 */
	public void registe(NioSocketClient nsc) {
		this.nsc = nsc;
		nsc.addRequestBind(this);
	}
	/**
	 * 返回数据
	 * 该如何处理？如何与nsc通信
	 */
	byte[] response;
	/**
	 * 添加返回值，由客户端调用。
	 * 每一包解析，当返回值完整，就结束挂起。
	 */
	public abstract void addResponseData(byte[] d);
//	{
//		response = d;
//		// 如果数据
//		countDownLatchReqResp.countDown();
//	}
	
	public Object doRequest(SocketExchange se) {
		// 设置锁
//		nsc.setCountDownLatchReqResp(countDownLatchReqResp);
		// 调用客户端发请求
		nsc.sendData(se.toByte());
		try {
			countDownLatchReqResp.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 如果
		// 返回后
		
		return parseObject(response);
	}
	
	
	public abstract Object parseObject(byte[] d);
	
}
