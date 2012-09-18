package cn.edu.pku.ss.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cn.edu.pku.ss.exception.LoginFailure;
import cn.edu.pku.ss.exception.MessageFormatError;
import cn.edu.pku.ss.safe.MD5andKL;
import cn.edu.pku.ss.util.ConstMessage;
import cn.edu.pku.ss.util.WebSocket;

public class DIAController {
	URI uri;

	WebSocket ws;

	String username;

	String password;

	// edit by lin-wei-long
	String app_num;
	// edit by lin-wei-long

	boolean conn = false;

	public DIAController(String s, String username, String password) {
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

	public void close() throws IOException {
		if (!conn)
			return;
		else {
			ws.close();
			conn = false;
		}

	}

	public void init() throws LoginFailure {
		try {
			ws.connect();
			// edit by lin-wei-long
			//System.out.println("---------->"+ username + password);
			ws.send(MD5andKL.KL("connect:" + username + password));
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String s = new String(bs);
			s  = MD5andKL.JM(s);
			
			//System.out.println("---------->"+s);
			if (!s.startsWith(ConstMessage.SUCCESS)) {
				ws.close();
				throw new LoginFailure();
			}
			// edit by lin-wei-long
			app_num = s.replaceAll("Successful:", "");
			// edit by lin-wei-long
			conn = true;
			System.out.println("Login Successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean sendCommand(String sensorID, String command)
			throws LoginFailure, MessageFormatError {
		if (!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long begin
			String temp = app_num + "@control:topic=Command" + "&sensorID="
					+ sensorID + "&command=" + command;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long end
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss = MD5andKL.JM(ss);
			
			if (ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_COMMAND_PERMISSION);
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
