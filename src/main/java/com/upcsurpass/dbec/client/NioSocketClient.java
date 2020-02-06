package com.upcsurpass.dbec.client;

import java.io.IOException;
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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.domain.SynchronizedMethod;
import com.upcsurpass.dbec.tools.ByteTools;

public class NioSocketClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(NioSocketClient.class);

	protected NioSocketClient() {
	}

	// private final static String TAG = "NioSocketClient";
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
	 * 调度锁，应该每个频道1个，现在只有1个。 因为只有1个状态，所以初始值为1
	 */
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	/**
	 * 频道，应该有多个。 现在只有1个。 如果
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

	public NioSocketClient(String ip, int port) {
		this.serverIp = ip;
		this.port = port;
	}

	// public static NioSocketClient create(String serverIp, int port) {
	// return new NioSocketClient(serverIp, port);
	// }

	/**
	 * 发送任务处理线程
	 * 
	 * @param data
	 *            byte[]
	 */
	public void sendData(final byte[] data) {
		if (data == null) {
			LOGGER.error("sendData -> 要发送的数据为空。");
			return;
		}
		try {
			LOGGER.debug("sendData => " + data);
			if(sendDataQueue.offer(data)) { // 向队列里插入一个请求数据
				LOGGER.debug("向队列中写入数据成功。当前队列中数量=> "+sendDataQueue.size());
			}else {
				LOGGER.debug("向队列中写入数据失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1 启动连接及读数据监听线程； 2 启动发数据监听线程；
	 */
	public void start() {
		// LOGGER.debug("启动start() 当前线程=> "+Thread.currentThread().getName());
		executor.execute(() -> { // 启动发送数据线程
			try {
//				LOGGER.debug("启动发送数据线程 当前线程=> " + Thread.currentThread().getName());
//				LOGGER.debug("当前countDownLatch => " + countDownLatch.toString());
				countDownLatch.await(); // 等待当前正在发送线程的执行完成
				while (!isShutDown) { // 如果还没有关闭
					if (socketChannel != null && socketChannel.isConnected()) { // 如果当前有连接有channel
						LOGGER.debug("socketChannel不为空并且已经连接成功。执行发送数据线程任务。当前线程->" + Thread.currentThread().getName());
						LOGGER.debug("socketChannel不为空并且已经连接成功。当前countDownLatch => " + countDownLatch.toString());
						byte[] data = null;
						try {
//							LOGGER.debug("当前isShutDown=>"+isShutDown);
//							Thread.sleep(100);
							LOGGER.debug("当前isShutDown=>"+isShutDown);
							if (!isShutDown) {
								LOGGER.debug("循环等待。要从任务队列中取数据。当前队列数量=> "+sendDataQueue.size());
								LOGGER.debug("循环等待。要从任务队列中取数据。当前线程-> "+Thread.currentThread().getName());
								data = sendDataQueue.take(); // 从队列中取出一个要发送的数据
								
							} else {
								LOGGER.debug("发送数据线程，收到关闭指令，关闭返回。当前线程-> "+Thread.currentThread().getName());
								return;
							}
						} catch (Exception e) {
//							LOGGER.debug("从任务队列中取数据异常");
							e.printStackTrace();
						}
						if (data != null && data.length > 0) {
							LOGGER.debug("发送数据线程 当前线程 => " + Thread.currentThread().getName() + " .");
							LOGGER.debug("发送数据线程 待发送数据 => " + data + " .");
							sendDataBuffer.clear(); // 清除上一次的数据
							sendDataBuffer.put(data); // 写上这一次的数据
							sendDataBuffer.flip(); // 缓冲区就绪
							/**
							 * TODO: 一个socket可以有多个channel，这里先只1个。
							 */
							if(socketChannel.isConnected()) {
								LOGGER.info("发送数据线程 当前连接正常 准备写入数据");
								socketChannel.write(sendDataBuffer); // 向频道中写入
							} else {
								LOGGER.error("发送数据线程 当前连接已经关闭");
							}
						} else {
							LOGGER.error("发送数据线程 -> 要发送的数据为空");
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
				/**
				 * 一个selector可以绑定多个socketChannel，现在先做一个。
				 */
				socketChannel = SocketChannel.open(); // 打开一个channel
				socketChannel.configureBlocking(false); // 配置为非阻塞
				selector = Selector.open(); // 打开一个选择器
				SocketAddress socketAddress = new InetSocketAddress(serverIp, port); //
				// 连接服务端socket
				socketChannel.connect(socketAddress); // 将这个频道连接到服务器地址的
				socketChannel.register(selector, SelectionKey.OP_CONNECT); // 注册选择器
//				LOGGER.debug("启动及接收数据线程,当前线程->" + Thread.currentThread().getName());
//				LOGGER.debug("启动及接收数据线程,socketChannel=>" + socketChannel.toString() + " - 1 - " + socketChannel.hashCode());
				// LOGGER.debug("启动及接收数据线程,socketChannel=>"+socketChannel.toString()+" - 2 -
				// "+socketChannel.hashCode());
//				LOGGER.debug("启动及接收数据线程,selector=>" + selector.toString());
				// selector处理
				while (!isShutDown) { // 如果没有关闭就循环执行
					selector.select(); // 选择器选择。监听所有注册的Channel，一直阻塞知道有任何一个客户端Channel有相应的事件到达
					Set<SelectionKey> keys = null;
					try {
						if (!isShutDown)
							keys = selector.selectedKeys();
						else
							return;
					} catch (Exception e) {
						LOGGER.debug("选择器异常");
						e.printStackTrace();
					}
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
							LOGGER.debug("接收数据线程,接收到了信号：key=>" + key.toString() + "当前线程->"
									+ Thread.currentThread().getName());
							// accept(key, selector); // 注册该客户端
						} else if (key.isConnectable()) { // 接收到了OP_CONNECT连接事件
							if (client.finishConnect()) {
								client.configureBlocking(false);
								client.register(selector, SelectionKey.OP_READ);
								LOGGER.debug("与服务端口连接成功。当前线程->" + Thread.currentThread().getName());
//								LOGGER.debug("与服务端口连接成功。当前countDownLatch => " + countDownLatch.toString());
								countDownLatch.countDown(); // 减小计数以使这个连接可发送数据
							}
						} else if (key.isReadable()) { // 接收到了OP_READ有返回数据可读事件
							LOGGER.debug("===========收到可读信号事件，开始读数据... 当前线程 => " + Thread.currentThread().getName());
							read(client);
							LOGGER.debug("===========收到可读信号事件，读数据结束... 当前线程 => " + Thread.currentThread().getName());

						} else if (key.isValid() && key.isWritable()) {// 如果可往客户端连接中写入数据，则处理该事件
							LOGGER.debug("===========收到可写信号事件，当前线程 => " + Thread.currentThread().getName());
							// write(key);
						} 
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * TODO: 现在有错误。由于可能两个线程都收到了读数据的信号，就会执行两遍读数据。
	 * 
	 * @param client
	 * @throws IOException
	 */
	private void read(SocketChannel client) throws IOException {
		LOGGER.debug("返回值 ：read(SocketChannel client) 当前线程=> " + Thread.currentThread().getName());
		ByteBuffer dataBuffer = ByteBuffer.allocate(DATA_MAX_LEN); // 分配缓冲区
		dataBuffer.clear();
		int readLen = client.read(dataBuffer);
		if(readLen<=0) {
			LOGGER.debug("读取返回值 ：readLen<=0 当前线程=> " + Thread.currentThread().getName());
			return;
		}
		byte[] result = new byte[readLen];
		dataBuffer.flip();
		dataBuffer.get(result);
		LOGGER.debug("返回值 ：" + ByteTools.byteArrToHexString(result));
		// 通知处理数据
		SocketExchange se = new SocketExchange(result);
		/**
		 * TODO: 1、应该根据se里的命令ID等信息，决定分配给哪一个。 现在DBES还不支持。 2、应该根据Client（channel决定结果发给谁）
		 */
		if (synchronizedMethod != null)
			synchronizedMethod.addResponseData(se);
		else
			LOGGER.debug("no synchronizedMethod");
		LOGGER.debug("返回值 ：read(SocketChannel client) 当前线程=> " + Thread.currentThread().getName());
		return;
	}

	/**
	 * 关闭该客户端
	 */
	public void shutDown() {
		isShutDown = true;
		// 需要发磅一个停止信号，一个空信号，以解除队列中的等待线程状态。
		byte[] shutDownData = {};
		sendDataQueue.offer(shutDownData);
		LOGGER.debug("关闭 -> " + System.currentTimeMillis() + "当前线程->" + Thread.currentThread().getName());
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
		executor.shutdownNow();
        // (所有的任务都结束的时候，返回TRUE)  
        try {
			if(!executor.awaitTermination(500, TimeUnit.MILLISECONDS)){  
			    // 超时的时候向线程池中所有的线程发出中断(interrupted)。  
				executor.shutdownNow();  
			}
		} catch (InterruptedException e) {
			LOGGER.debug("关闭时发生Interrupt异常");
			e.printStackTrace();
		}  
	}

	/**
	 * SynchronizedMethod应该绑定在Channel上。 现在只接受一个SynchronizedMethod
	 */
	SynchronizedMethod synchronizedMethod;

	public void addRequestBind(SynchronizedMethod synchronizedMethod) {
		this.synchronizedMethod = synchronizedMethod;
	}
}
