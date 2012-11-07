package cn.edu.pku.ss.starter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/**
 * 
 * @author qhwang
 * This class is not used now!
 */
public class PhoneEventProcessor {
	private static final String QHWANG_PHONE ="E0:D7:BA:F2:2C:FD";
	private static final String RTANG_PHONE ="0C:74:C2:14:BB:9B";
	private static final String SERVER_IP="192.168.11.18";
	
	private Socket clientSocket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public boolean sendMessageToSocketServer(String bluetoothAddress){
		String msg;
		String receivedInfo;
		if(bluetoothAddress.equals(QHWANG_PHONE)){
			msg="11.mp3";
		}else if(bluetoothAddress.equals(RTANG_PHONE)){
			msg="22.mp3";
		}
		else{
			msg="33.mp3";
		}
		
		if(!connectToServerSocket())
			return false;
		else{
			try {
				dos.writeUTF(msg);
				
				while(!(receivedInfo = dis.readUTF()).equals("SUCCESS")){
					dos.writeUTF(msg);
				}
				dos.close();
				dis.close();
				clientSocket.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	private boolean connectToServerSocket(){
		try {
			clientSocket=new Socket(SERVER_IP,8880);
			InputStream is = clientSocket.getInputStream();
			dis = new DataInputStream(is);
			OutputStream os = clientSocket.getOutputStream();
			dos = new DataOutputStream(os);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
