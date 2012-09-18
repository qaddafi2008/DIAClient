package cn.edu.pku.ss.eventProcessor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.edu.pku.ss.bean.DIALocationMessage;
import cn.edu.pku.ss.exception.LoginFailure;
import cn.edu.pku.ss.exception.MessageFormatError;
import cn.edu.pku.ss.exception.NoPermissionException;
import cn.edu.pku.ss.safe.MD5andKL;
import cn.edu.pku.ss.util.ConstMessage;
import cn.edu.pku.ss.util.WebSocket;
import dk.itu.infobus.ws.Listener;
import dk.itu.infobus.ws.ListenerToken;

public class EventProcessor {
	URI uri;

	WebSocket ws;

	String username;

	String password;

	// edit by lin-wei-long
	String app_num;

	// edit by lin-wei-long
	boolean conn = false;
	boolean eventPermission = false;

	public EventProcessor(String s, String username, String password) {
		try {
			uri = new URI(s);
			ws = new WebSocket(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// edit by lin-wei-long
		this.username = MD5andKL.MD5(username);
		this.password = MD5andKL.MD5(password);
		// edit by lin-wei-long
	}

	public void init() throws LoginFailure {
		try {
			ws.connect();
			// edit by lin-wei-long
			ws.send(MD5andKL.KL("connect:" + username + password));
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String s = new String(bs);
			s = MD5andKL.JM(s);
			if (!s.startsWith(ConstMessage.SUCCESS)) {
				ws.close();
				throw new LoginFailure();
			}
			
			app_num = s.replaceAll("Successful:", "");// edit by lin-wei-long

			conn = true;
			System.out.println("Login Successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean addEventGenerator(String topic) throws LoginFailure,
			MessageFormatError {
		if (!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long
			String temp = app_num + "@addGenerator:topic=" + topic;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);
			if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			} else if(ss.equals(ConstMessage.NO_PERMISSION)){
//				System.out.println("我们没有权限");
				System.out.println(ConstMessage.NO_EVENT_PERMISSION);
				return false;
			}else if (ss.equals(ConstMessage.SUCCESS)) {
				System.out.println("Successful");
				eventPermission = true;
				return true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public Thread addListener(final DIAListener listener,String epl) throws LoginFailure,NoPermissionException {
		if(!conn)
			throw new LoginFailure();
		if(!eventPermission)
			throw new NoPermissionException();
		
		Thread t=null;
		try {
			// Send command to the server. The command contains the EPL,Such as
			// "Select * From Temperature where Temperature > 10"

			// edit by lin-wei-long
			String temp = app_num + "@addListener:epl="+epl;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			// new a thread for the result.
			t = new Thread(){
				public void run() {
					System.out.println("Listener Start!!");
					while(true) {
						byte[] bs;
						try {
							// When receive message from server, it will parse
							// it.
							bs = ws.recv();
							System.out.println("Received: "+bs);
							String ss = new String(bs);
							ss = MD5andKL.KL(ss);
							// The listener has started on the server, so the
							// listener on the client should start too.
							if(ss.equals("Start")) {
								listener.onStarted();
							}
							else if(ss.equals("CleanUp")) {
								try {
									listener.cleanUp();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else {
								// The listener is trigered on the server. Then,
								// call the onMessage() and use the messge as
								// the parameter.
								System.out.println(ss);
								Gson gson = new Gson();
								Type type = new TypeToken<Map<String, String>>(){}.getType();
								Map<String, String> ms= gson.fromJson(ss, type);
								 type = new TypeToken<String>(){}.getType();
								String t =   (String)(ms.get("Temperature"));
								System.out.println(t);
								listener.onMessage(ms);
								return;// exist the thread.
						}
						}catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			t.start();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 
	 * @param sensorID
	 *            传感器id
	 * @param command
	 *            控制命令
	 * @return 返回发送成功(true)/失败(false)
	 * @throws LoginFailure
	 * @throws MessageFormatError
	 */
	public boolean sendCommand(String sensorID, String command)
			throws LoginFailure, MessageFormatError {
		if (!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long
			String temp = app_num + "@control:topic=Command" + "&sensorID="
					+ sensorID + "&command=" + command;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long

			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss = MD5andKL.JM(ss);
			if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_EVENT_PERMISSION);
				return false;
			}else if (ss.equals(ConstMessage.SUCCESS)) {
				System.out.println("Successful");
				return true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
