package com.upcsurpass.dbec.client;

public class PooledNIOSocketClient extends NioSocketClient {

	private boolean busy = false; // 此对象是否正在使用的标志，默认没有正在使用

	// 构造函数，池化对象
	public PooledNIOSocketClient() {
	}

	// 获得对象对象是否忙
	public boolean isBusy() {
		return busy;
	}

	// 设置对象的对象正在忙
	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public void close() {
		this.shutDown();
	}

}