package com.upcsurpass.dbec.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.domain.SynchronizedMethod;
import com.upcsurpass.dbec.tools.ByteTools;

public class NioSocketClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(NioSocketClient.class);

	protected NioSocketClient() {}
//	private final static String TAG = "NioSocketClient";
	/**
	 * 一个数据包的最大量
	 */
	private final static int DATA_MAX_LEN = GlobalConsts.MAXLEN_EXCHANGEBUFFER;// 0xFFFF;
	/**
	 * 服务端IP和端口
	 */
	private String serverIp;
	private int port;
	/**
	 * 执行线程 2个
	 */
	private final ExecutorService executor = Executors.newFixedThreadPool(2);
	/**
	 * 是否已经关闭
	 */
	private volatile boolean isShutDown = false;
	/**
	 * 调度锁 初始值为1
	 */
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	/**
	 * 频道 只有1个
	 */
	private SocketChannel socketChannel;
	/**
	 * 选择器 只有1个，其实可以对应多个频道
	 */
	private Selector selector;
	/**
	 * 发送数据序列，可以发送100个请求排队
	 */
	private final BlockingQueue<byte[]> sendDataQueue = new ArrayBlockingQueue<byte[]>(100);
	/**
	 * 发送数据缓冲
	 */
	private final ByteBuffer sendDataBuffer = ByteBuffer.allocate(DATA_MAX_LEN);

	private NioSocketClient(String ip, int port) {
		this.serverIp = ip;
		this.port = port;
	}

	public static NioSocketClient create(String serverIp, int port) {
		return new NioSocketClient(serverIp, port);
	}

	/**
	 * 发送任务处理线程
	 * 
	 * @param data
	 *            byte[]
	 */
	public void sendData(final byte[] data) {
		if (data == null) {
			return;
		}
		try {
			LOGGER.info("sendData => " + data);
			sendDataQueue.offer(data); // 向队列里插入一个请求数据
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1 启动连接及读数据监听线程； 2 启动发数据监听线程；
	 */
	public void start() {
		LOGGER.info("start()");
		executor.execute(() -> { // 启动发送数据线程
			try {
				countDownLatch.await(); // 等待当前正在发送线程的执行完成
				while (!isShutDown) { // 如果还没有关闭
					if (socketChannel != null && socketChannel.isConnected()) { // 如果当前有连接有channel
						LOGGER.info("executor.execute(()");
						byte[] data = sendDataQueue.take(); // 从队列中取出一个要发送的数据
						if (data != null && data.length > 0) {
							sendDataBuffer.clear(); // 清除上一次的数据
							sendDataBuffer.put(data); // 写上这一次的数据
							sendDataBuffer.flip(); // 缓冲区就绪
							socketChannel.write(sendDataBuffer); // 向频道中写入
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		// 启动及接收数据线程
		executor.execute(() -> {
			try {
				LOGGER.info("启动及接收数据线程");

				socketChannel = SocketChannel.open(); // 打开一个channel
				socketChannel.configureBlocking(false); // 配置为非阻塞
				selector = Selector.open(); // 打开一个选择器
				SocketAddress socketAddress = new InetSocketAddress(serverIp, port); //
				// 连接服务端socket
				socketChannel.connect(socketAddress); // 将这个频道连接到服务器地址的
				socketChannel.register(selector, SelectionKey.OP_CONNECT); // 注册选择器
				// 数据缓冲
				ByteBuffer dataBuffer = ByteBuffer.allocate(DATA_MAX_LEN); // 分配缓冲区
				// selector处理
				while (!isShutDown) { // 如果没有关闭就循环执行
					selector.select(); // 选择器选择。监听所有注册的Channel，一直阻塞知道有任何一个客户端Channel有相应的事件到达
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> keyIterator = keys.iterator();
					SocketChannel client;
					while (keyIterator.hasNext()) {
						SelectionKey key = keyIterator.next();
						// 这里需要注意的是，Selector在为每个有事件到达的Channel建立SelectionKey对象
				        // 之后，其并不会将其移除，如果我们不进行移除，那么下次循环时该事件还会再被处理一次，
				        // 因而这里要调用remove()方法移除该SelectionKey
						keyIterator.remove(); // 
						client = (SocketChannel) key.channel(); // 返回当前SelectionKey中所封装的Channel对象
						
				        if (key.isAcceptable()) {// 如果是有新的客户端Channel连接建立，则处理该事件
//				          accept(key, selector); // 注册该客户端
				        } else if (key.isConnectable()) { // 接收到了OP_CONNECT连接事件
							// PLog.d(TAG,"与服务端口["+serverIp+":"+port+"]连接成功");
							if (client.finishConnect()) {
								client.configureBlocking(false);
								client.register(selector, SelectionKey.OP_READ);
								countDownLatch.countDown(); // 减小计数以使这个连接可发送数据
							}
						} else if (key.isReadable()) { // 接收到了OP_READ有返回数据可读事件
							dataBuffer.clear();
							int readLen = client.read(dataBuffer);
							byte[] result = new byte[readLen];
							dataBuffer.flip();
							dataBuffer.get(result);
							LOGGER.info("返回值 ："+ByteTools.byteArrToHexString(result));
							// 通知处理数据
							if(synchronizedMethod!=null)
								synchronizedMethod.addResponseData(result);
							else
								LOGGER.info("no synchronizedMethod");
						} else if (key.isValid() && key.isWritable()) {// 如果可往客户端连接中写入数据，则处理该事件
//					            write(key);
					    }
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 关闭该客户端
	 */
	public void shutDown() {
		try {
			if (socketChannel != null) {
				socketChannel.close();
			}
			if (selector != null) {
				selector.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isShutDown = true;
		executor.shutdownNow();
	}


	/**
	 * 现在只接受一个SynchronizedMethod
	 */
	SynchronizedMethod synchronizedMethod;
	public void addRequestBind(SynchronizedMethod synchronizedMethod) {
		this.synchronizedMethod = synchronizedMethod;
	}
}
