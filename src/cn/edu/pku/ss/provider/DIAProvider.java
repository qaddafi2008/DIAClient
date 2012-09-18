package cn.edu.pku.ss.provider;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.pku.ss.bean.DIAGasMessage;
import cn.edu.pku.ss.bean.DIAHumidityMessage;
import cn.edu.pku.ss.bean.DIALocationMessage;
import cn.edu.pku.ss.bean.DIAPhoneMessage;
import cn.edu.pku.ss.bean.DIATemperatureMessage;
import cn.edu.pku.ss.exception.LoginFailure;
import cn.edu.pku.ss.exception.MessageFormatError;
import cn.edu.pku.ss.safe.MD5andKL;
import cn.edu.pku.ss.util.ConstMessage;
import cn.edu.pku.ss.util.MyJsonDeserializer;
import cn.edu.pku.ss.util.WebSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class DIAProvider {
	URI uri;
	WebSocket ws;
	String username;
	String password;
	// edit by lin-wei-long
	String app_num;
	// edit by lin-wei-long
	boolean conn = false;
	private Gson gson = new GsonBuilder()
    .registerTypeAdapter(Object.class, new MyJsonDeserializer()).create();
	
	public DIAProvider(String wsurl,String username,String password) {
		try {
			uri = new URI(wsurl);
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
		if(!conn)
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
			ws.send(MD5andKL.KL("connect:" + username + password));
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String s = new String(bs);
			s=MD5andKL.JM(s);//edit by lin-wei-long
			
			if(!s.startsWith(ConstMessage.SUCCESS)) {
				ws.close();
				throw new LoginFailure();
			}
			else {
				// edit by lin-wei-long
				app_num = s.replaceAll("Successful:", "");
				// edit by lin-wei-long
				conn =true;
				System.out.println("Login Successfully");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<DIALocationMessage> getLocation(String s,int kind) throws LoginFailure,MessageFormatError {
		if(!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long
			String temp = app_num + "@query:topic=Location&kind="+kind+"&key="+s;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);//edit by lin-wei-long
			
			if(ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_QUERY_PERMISSION);
			}else if(ss.equals("null")) {
				return null;
			}else {
				// Gson gson = new Gson();
				Type type = new TypeToken<List<DIALocationMessage>>(){}.getType();
				List<DIALocationMessage> list = gson.fromJson(ss, type);
				return list;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<DIATemperatureMessage> getTemperature(String s,int kind) throws LoginFailure, MessageFormatError {
		if(!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long
			String temp = app_num + "@query:topic=Temperature&kind="+kind+"&key="+s;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);//edit by lin-wei-long
			
			if(ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_QUERY_PERMISSION);
			}
			else if(ss.equals("null")) {
				return null;
			}
			else {
				// Gson gson = new Gson();
				Type type = new TypeToken<List<DIATemperatureMessage>>(){}.getType();
				List<DIATemperatureMessage> list = gson.fromJson(ss, type);
				return list;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<DIAHumidityMessage> getHumidity(String s,int kind) throws LoginFailure, MessageFormatError {
		if(!conn)
			throw new LoginFailure();
		try {
			// edit by lin-wei-long
			String temp = app_num + "@query:topic=Humidity&kind="+kind+"&key="+s;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);//edit by lin-wei-long
			
			if(ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_QUERY_PERMISSION);
			}else if(ss.equals("null")) {
				return null;
			}
			else {
				// Gson gson = new Gson();
				Type type = new TypeToken<List<DIAHumidityMessage>>(){}.getType();
				List<DIAHumidityMessage> list = gson.fromJson(ss, type);
				return list;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public List<DIAGasMessage> getGasStatus(String s,int kind) throws LoginFailure, MessageFormatError {
		if(!conn)
			throw new LoginFailure();
		try {
			//edit by lin-wei-long
			String temp = app_num + "@query:topic=Gas&kind="+kind+"&key="+s;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);//edit by lin-wei-long
			
			if(ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_QUERY_PERMISSION);
			}else if(ss.equals("null")) {
				return null;
			}
			else {
				// Gson gson = new Gson();
				Type type = new TypeToken<List<DIAGasMessage>>(){}.getType();
				// System.out.println("ss: "+ss);
				List<DIAGasMessage> list = gson.fromJson(ss, type);
				/*
				 * for(int i=0;i<list.size();i++){ Object
				 * object=list.get(i).getExtendElements().get("Gas");
				 * System.out.println(i+": "+String.valueOf(object)); }
				 */
				return list;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<DIAPhoneMessage> getPhoneStatus(String s,int kind) throws LoginFailure, MessageFormatError {
		if(!conn)
			throw new LoginFailure();
		try {
//			 edit by lin-wei-long
			String temp = app_num + "@query:topic=Phone&kind="+kind+"&key="+s;
			temp = MD5andKL.KL(temp);
			ws.send(temp);
			// edit by lin-wei-long
			byte[] bs = ws.recv();
			String ss = new String(bs);
			ss=MD5andKL.JM(ss);//edit by lin-wei-long
			
			if(ss.equals(ConstMessage.WRONG_MESSAGE)) {
				// ws.close();
				throw new MessageFormatError();
			}else if(ss.equals(ConstMessage.NO_PERMISSION)){
				System.out.println(ConstMessage.NO_QUERY_PERMISSION);
			}else if(ss.equals("null")) {
				return null;
			}
			else {
				Type type = new TypeToken<List<DIAPhoneMessage>>(){}.getType();
				List<DIAPhoneMessage> list = gson.fromJson(ss, type);
				return list;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args){
		Gson gson3 = new GsonBuilder()
	    .registerTypeAdapter(Object.class, new MyJsonDeserializer()).create();
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", 1);
		map.put("b", "a");
		map.put("c", 2);
		Test test = new Test();
		test.id = 12345;
		test.name = "tom";
		test.map = map;
		Gson gson2 = new Gson();
		String mapString=gson3.toJson(test);
		// Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
		// Map<String, Object> deserializedMap = gson2.fromJson(mapString,
		// type);
		Type type = new TypeToken<Test>(){}.getType();
		Test deserializedTest = gson3.fromJson(mapString, type);
		System.out.println(deserializedTest.id);
		System.out.println(deserializedTest.name);
		Map<String, Object> deserializedMap = deserializedTest.map;
		Iterator<String> it = deserializedMap.keySet().iterator();
		while(it.hasNext()){
			System.out.println(deserializedMap.get(it.next()));
		}
	}
}
class Test{
	public int id;
	public String name;
	public Map<String, Object> map;
}
