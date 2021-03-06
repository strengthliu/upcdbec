package com.upcsurpass.dbec.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.App;
import com.upcsurpass.dbec.appCfg.GlobalConsts;
import com.upcsurpass.dbec.domain.SocketExchange;
import com.upcsurpass.dbec.service.method.GetServerCurrentTime;

public class SocketClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketClient.class);

	public static final String IP_ADDR = "192.168.43.33";// 服务器地址
	public static final int PORT = GlobalConsts.DEFAULT_DBESERVERPORT;// 服务器端口号

	public static void main(String[] args) {
		System.out.println("客户端启动...");
		System.out.println("==========================================================");
		sendR();
	}

	public static void sendR() {
		Socket socket = null;
		try {
			// 创建一个流套接字并将其连接到指定主机上的指定端口号
			socket = new Socket("192.168.43.33", 51024);

			// 从服务器读数据的流
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// 向服务器发送数据的流
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			// 准备参数
			byte[] data = { 21, 1, 4, 0, 0, 78, 85, 76, 76 };
            byte[] data1 = { 'N', 'U', 'L', 'L' };
    			SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME, 1, 4, 0, 0);
    			se.setBuffer(data1);

			for (int i = 0; i < 2; i++) {
				System.out.println("要发送的数据是： "+se);
				out.write(se.toByte());
				System.out.println("=》=》=》=》=》=》=》=》=》 发送完成");

				byte[] b = new byte[GlobalConsts.MAXLEN_EXCHANGEBUFFER];
				input.read(b);
				System.out.println("《=《=《=《==《=《=《=《=  服务器端返回过来的是: " + b);
				System.out.println("==========================================================");
			}

			// 关闭流，结束测试
			out.close();
			input.close();
		} catch (Exception e) {
			System.out.println("客户端异常:" + e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println("客户端 finally 异常:" + e.getMessage());
				}
			}
		}

	}

	public static boolean testConnect(String serverName, int port) {
		Socket socket = null;
		try {
			LOGGER.debug("testConnect 准备连接");
			// 创建一个流套接字并将其连接到指定主机上的指定端口号
			socket = new Socket(serverName, port);
			LOGGER.debug("testConnect 连接完成");

			// 从服务器读数据的流
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// 向服务器发送数据的流
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			// 准备参数
			byte[] data = { 21, 1, 4, 0, 0, 78, 85, 76, 76 };
            byte[] data1 = { 'N', 'U', 'L', 'L' };
    			SocketExchange se = new SocketExchange(GlobalConsts.DBEC_GETSERVERCURRENTTIME, 1, 4, 0, 0);
    			se.setBuffer(data1);

			for (int i = 0; i < 1; i++) {
				LOGGER.debug("要发送的数据是： "+se);
				out.write(se.toByte());
				LOGGER.debug("=》=》=》=》=》=》=》=》=》 发送完成");

				byte[] b = new byte[GlobalConsts.MAXLEN_EXCHANGEBUFFER];
				input.read(b);
				LOGGER.debug("《=《=《=《==《=《=《=《=  服务器端返回过来的是: " + b);
				LOGGER.debug("==========================================================");
			}

			// 关闭流，结束测试
			out.close();
			input.close();
			
		} catch (Exception e) {
			LOGGER.debug("客户端异常:" + e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
					LOGGER.debug("连接测试成功，完成，返回。");
					return true;
				} catch (IOException e) {
					socket = null;
					LOGGER.debug("客户端 finally 异常:" + e.getMessage());
					return false;
				}
			}
		}
		return false;
	}
}
