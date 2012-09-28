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
import com.sun.org.apache.regexp.internal.recompile;

import cn.edu.pku.ss.bean.DIALocationMessage;
import cn.edu.pku.ss.exception.LoginFailure;
import cn.edu.pku.ss.exception.MessageFormatError;
import cn.edu.pku.ss.exception.NoPermissionException;
import cn.edu.pku.ss.safe.MD5andKL;
import cn.edu.pku.ss.util.ConstMessage;
import cn.edu.pku.ss.util.WebSocket;
import dk.itu.infobus.ws.Listener;
import dk.itu.infobus.ws.ListenerToken;

/**
 * 
 * @author qinhuiwang use this processor, the listener can continue working
 */
public class ContinuedEventProcessor {
	private URI uri;
	private WebSocket ws;
	private String username;
	private String password;
	private boolean conn = false;
	private boolean eventPermission = false;
	private String app_num;
	private static ContinuedEventProcessor singleton = null;
	
	public static ContinuedEventProcessor getInstance(String s, String username, String password)throws LoginFailure{
		if(null == singleton){
			singleton=new ContinuedEventProcessor(s, username, password);
			singleton.init();
		}
		return singleton;
	}

	private ContinuedEventProcessor(String s, String username, String password) {
		try {
			uri = new URI(s);
			ws = new WebSocket(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.username = MD5andKL.MD5(username);
		this.password = MD5andKL.MD5(password);
	}

	private void init() throws LoginFailure {
		try {
			ws.connect();
			ws.send(MD5andKL.KL("connect:" + username + password));
			byte[] bs = ws.recv();
			String s = new String(bs);
			s = MD5andKL.JM(s);// edit by lin-wei-long

			if (!s.startsWith(ConstMessage.SUCCESS)) {
				ws.close();
				throw new LoginFailure();
			} else {
				app_num = s.replaceAll("Successful:", "");
				conn = true;
				System.out.println("Login Successfully");
			}
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
			ws.send(MD5andKL.KL(app_num + "@addGenerator:topic=" + topic));
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss = MD5andKL.JM(ss);
			if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			} else if (ss.equals(ConstMessage.NO_PERMISSION)) {
				System.out.println(ConstMessage.NO_EVENT_PERMISSION);
				return false;
			} else if (ss.equals(ConstMessage.SUCCESS)) {
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

	public void addListener(String epl)
			throws LoginFailure, NoPermissionException {
		if (!conn)
			throw new LoginFailure();
		if (!eventPermission)
			throw new NoPermissionException();
		
		try {
			// Send command to the server. The command contains the EPL,Such as
			// "Select * From Temperature where Temperature > 10"
			ws.send(MD5andKL.KL(app_num + "@addListener:epl=" + epl));
			// new a thread for the result.
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Thread startListener(final DIAListener listener){
		Thread t = null;

		// new a thread for the result.
		t = new Thread() {
			public void run(){
				System.out.println("Listener Thread Start!!");
				while (true) {
					byte[] bs;
					try {
						// When receive message from server, it will parse
						// it.
						bs = ws.recv();
						//System.out.println("Received: " + bs);
						String ss = new String(bs);
						ss = MD5andKL.JM(ss);
						//System.out.println("ContinuedEventProcessor Get: " + ss);
						// The listener has started on the server, so the
						// listener on the client should start too.
						if (ss.equals("Start")) {
							listener.onStarted();
						} else if (ss.equals("CleanUp")) {
							try {
								listener.cleanUp();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
							System.err.println("The message format is wrong!");
						} else if(ss.equals(ConstMessage.NO_PERMISSION)){
							System.err.println(ConstMessage.NO_COMMAND_PERMISSION);
							
						}else if (ss.equals(ConstMessage.SUCCESS)) {
							System.out.println("Successful");
							
						}else {
							// The listener is trigered on the server. Then,
							// call the onMessage() and use the messge as
							// the parameter.
							Gson gson = new Gson();
							Type type = new TypeToken<Map<String, String>>() {}.getType();
							Map<String, String> ms = gson.fromJson(ss, type);
							//type = new TypeToken<String>() {}.getType();
							// String t = (String)(ms.get("Temperature"));
							// System.out.println(t);
							listener.onMessage(ms);
							// return;//exist the thread.
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

		return t;
	}

	/**
	 * 发送喇叭控制命令：给喇叭所在电脑发送对应音频的URL，并让电脑播放URL对应的音频文件。
	 * 
	 * @param command
	 *            控制命令
	 * @return 成功或失败
	 * @throws LoginFailure
	 * @throws MessageFormatError
	 */
	public boolean sendLoudspeakerCommand(String command) throws LoginFailure,
			MessageFormatError {
		if (!conn)
			throw new LoginFailure();
		try {
			synchronized (ws) {
				ws.send(MD5andKL.KL(app_num
						+ "@controlLoudspeaker:topic=Command" + "&command="
						+ command));
				/*
				 * byte[] bs = ws.recv(); String ss = new String(bs); if
				 * (ss.equals("Wrong message")) { // ws.close(); throw new
				 * MessageFormatError(); } else if (ss.equals("Successful")) {
				 * System.out.println("Successful"); return true; }
				 */
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 发送控制命令：根据蓝牙地址给对应的手机发送不同的音频所在的URL，并让手机自动播放
	 * 
	 * @param command
	 *            控制命令
	 * @return 发送成功或失败
	 * @throws LoginFailure
	 * @throws MessageFormatError
	 */
	public boolean sendAudioToPhoneCommand(String command) throws LoginFailure,
			MessageFormatError {
		if (!conn)
			throw new LoginFailure();
		try {
			synchronized (ws) {
				ws.send(MD5andKL.KL(app_num + "@sendAudioToPhone:topic=Command"
						+ "&command=" + command));
				/*
				 * byte[] bs = ws.recv(); String ss = new String(bs); if
				 * (ss.equals("Wrong message")) { throw new
				 * MessageFormatError(); } else if (ss.equals("Successful")) {
				 * System.out.println("Successful"); return true; }
				 */
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
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
			ws.send(MD5andKL.KL(app_num + "@control:topic=Command"
					+ "&sensorID=" + sensorID + "&command=" + command));
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss = MD5andKL.JM(ss);
			if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			} else if (ss.equals(ConstMessage.NO_PERMISSION)) {
				System.out.println(ConstMessage.NO_COMMAND_PERMISSION);
			} else if (ss.equals(ConstMessage.SUCCESS)) {
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
