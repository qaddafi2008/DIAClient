package cn.edu.pku.ss.starter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.edu.pku.ss.Temperature;
import cn.edu.pku.ss.bean.DIADoorContactMessage;
import cn.edu.pku.ss.bean.DIAGasMessage;
import cn.edu.pku.ss.bean.DIAHumidityMessage;
import cn.edu.pku.ss.bean.DIALocationMessage;
import cn.edu.pku.ss.bean.DIAPhoneMessage;
import cn.edu.pku.ss.bean.DIATemperatureMessage;
import cn.edu.pku.ss.bean.Kind;
import cn.edu.pku.ss.controller.DIAController;
import cn.edu.pku.ss.eventProcessor.ContinuedEventProcessor;
import cn.edu.pku.ss.eventProcessor.DIAListener;
import cn.edu.pku.ss.eventProcessor.EventProcessor;
import cn.edu.pku.ss.exception.LoginFailure;
import cn.edu.pku.ss.exception.MessageFormatError;
import cn.edu.pku.ss.exception.NoPermissionException;
import cn.edu.pku.ss.provider.DIAProvider;
import cn.edu.pku.ss.util.BluetoothCollection;
import cn.edu.pku.ss.util.Util;

public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 控制//control
		/*DIAController c = new DIAController("ws://localhost:8080/DIAServer/core-socket","jerry","2012727");
		try {
			c.init();
			Date date = new Date();
			System.out.println("Send command at "+date+" "+date.getTime());
			//c.sendCommand("14", "");
			//int i = 1;
			for(int i=0;i<5;i++)
				c.sendCommand("15", "1500002$");
			
		} catch (LoginFailure e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (MessageFormatError e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
		//信息查询//inquiry
	/*	DIAProvider p = new DIAProvider("ws://localhost:8080/DIAServer/core-socket","jerry","2012727");
		try {
			p.init();
		} catch (LoginFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			List<DIALocationMessage> list =null;
			List<DIATemperatureMessage> tList = null;
			List<DIAGasMessage> gList = null;
			List<DIAHumidityMessage> hList = null;
			List<DIADoorContactMessage> dList = null;
			List<DIAPhoneMessage> pList = null;
			
			try {
				list = p.getLocation("07", Kind.bySensorID);
				//gList = p.getGasStatus("06", Kind.bySensorID);//the user jerry don't have this permission
				//tList = p.getTemperature("12", Kind.bySensorID);
				//hList = p.getHumidity("13", Kind.bySensorID);
				
				//pList = p.getPhoneStatus("16", Kind.bySensorID);
				
				if(gList == null)
					System.out.println("yes,null");
				
				//System.out.println(list.get(0).getTopic());
			    System.out.println(list.get(0).getStatus());
			    System.out.println(list.get(1).getExtendElements().get("Location"));
			    //System.out.println(gList.get(0).getStatus());
			    //System.out.println(tList.get(0).getTemperature());
			    //System.out.println(tList.get(1).getTemperature());
			    //System.out.println(hList.get(0).getHumidity());
			    //System.out.println(pList.get(0).getStatus());
			} catch (LoginFailure e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessageFormatError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				p.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		//事件处理//event
		/*EventProcessor ep = new EventProcessor("ws://localhost:8080/DIAServer/core-socket","jerry","2012727");//new EventProcessor("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			ep.init();
			ep.addEventGenerator("Temperature");
		} catch (LoginFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessageFormatError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DIAListener l = new DIAListener(){
			public void cleanUp() throws Exception {
            }
            public void onStarted() {
                System.out.println("Listener started!");
            }
            public void onMessage(Map<String, String> msg) {
                Util.traceEvent(msg);
                Date date = new Date();
    			System.out.println("Notify the client at "+date+" "+date.getTime());
            	//Open Air Conditioning to let the room cool down
            }

	};
	try {
		ep.addListener(l, "Select * From Temperature where Temperature > 10");
	} catch (Exception e) {
		e.printStackTrace();
	}*/
		//事件处理——温度的循环监听//event-temperature
		ContinuedEventProcessor ep = new ContinuedEventProcessor("ws://localhost:8080/DIAServer/core-socket","jerry","2012727");//new EventProcessor("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			ep.init();
			ep.addEventGenerator("Temperature");//Gas
		} catch (LoginFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessageFormatError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DIAListener l = new DIAListener(){
			public void cleanUp() throws Exception {
            }
            public void onStarted() {
                System.out.println("Listener started!");
            }
            public void onMessage(Map<String, String> msg) {
                Util.traceEvent(msg);
                Date date = new Date();
    			System.out.println(msg.get(DIATemperatureMessage.VALUE_NAME)+"--Notify the client at "+date+" "+date.getTime());// DIAGasMessage.VALUE_NAME
            	//Open Air Conditioning to let the room cool down
            }

	};
	try {
		ep.addListener(l, "Select * From Temperature where Temperature > 10");////Select * From Gas
	} catch (LoginFailure e) {
		e.printStackTrace();
	}catch (NoPermissionException e) {
		e.printStackTrace();
	}
		
	
		//事件处理-手机//event-phone detected
		/*ContinuedEventProcessor ep = new ContinuedEventProcessor("ws://localhost:8080/DIAServer/core-socket","jerry","2012727");//new EventProcessor("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			ep.init();
			ep.addEventGenerator("Phone");
		} catch (LoginFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessageFormatError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BluetoothCollection.sendToLoudspeakerOrPhone = BluetoothCollection.SEND_TO_PHONE;//设置用于控制手机还是喇叭
		BluetoothCollection.init(ep);//初始化
		
		DIAListener l = new DIAListener(){
			private boolean test=true;
			public void cleanUp() throws Exception {
            }
            public void onStarted() {
                System.out.println("Listener started!");
            }
            public void onMessage(Map<String, String> msg) {
                Util.traceEvent(msg);
                Date date = new Date();
                String phoneAddress = msg.get(DIAPhoneMessage.VALUE_NAME);
                System.out.println("blue address:"+phoneAddress);
                BluetoothCollection.addBluetooth(phoneAddress);//将蓝牙添加到收集类中，由该类来做处理

    			System.out.println(phoneAddress+"--Notify the client at "+date+" "+date.getTime());
            	//Open Air Conditioning to let the room cool down
            }

	};
	try {
		ep.addListener(l, "Select * From Phone");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
		
/*		DIAController c = new DIAController("ws://192.168.1.107:8080/DIAServer/core-socket","wilson","1234");
		try {
			c.init();
			c.sendCommand("1102", "on");
		} catch (LoginFailure e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (MessageFormatError e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
		
		/*DIAProvider p = new DIAProvider("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			p.init();
			try {
				
				List<DIALocationMessage> list =p.getLocation("chuweijie-Iphone4", Kind.bySensorName);
				if(list != null) {
					System.out.println(list.get(0).getZoneLocation());
				}
				else {
					System.out.println("null");
				}
				list =p.getLocation("123456789021", Kind.bySensorID);
				if(list != null) {
					
					System.out.println(list.get(0).getZoneLocation());
				}
				else {
					System.out.println("null");
				}
				list = p.getLocation("5205", Kind.byZoneName);
				if(list != null) {
					
					System.out.println(list.get(0).getZoneLocation());
					System.out.println(list.size());
				}
				else {
					System.out.println("null");
				}
				
				List<DIATemperatureMessage> list2 = p.getTemperature("5205", Kind.byZoneName);
				if(list2 != null) {
					System.out.println(list2.get(0).getTemperature());
				}
				else {
					System.out.println("null");
				}
				list2 = p.getTemperature("123456789012", Kind.bySensorID);
				if(list2 != null) {
					System.out.println(list2.get(0).getTemperature());
				}
				else {
					System.out.println("null");
				}
				List<DIAHumidityMessage> list3 = p.getHumidity("5205", Kind.byZoneName);
				if(list3 != null) {
					System.out.println(list3.get(0).getHumidity());
				}
				else {
					System.out.println("null");
				}
				list3 = p.getHumidity("123456789231", Kind.bySensorID);
				if(list3 != null) {
					System.out.println(list3.get(0).getHumidity());
				}
				else {
					System.out.println("null");
				}
			} catch (MessageFormatError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (LoginFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EventProcessor ep = new EventProcessor("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			ep.init();
			ep.addEventGenerator("Temperature");
		} catch (LoginFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessageFormatError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DIAListener l = new DIAListener(){
			public void cleanUp() throws Exception {
            }
            public void onStarted() {
                System.out.println("Listener started!");
            }
            public void onMessage(Map<String, String> msg) {
                Util.traceEvent(msg);
            	//Open Air Conditioning to let the room cool down
            }

	};
	try {
		ep.addListener(l, "Select * From Temperature where Temperature > 10");
	} catch (LoginFailure e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

		/*DIAProvider p = new DIAProvider("ws://localhost:8080/DIAServer/core-socket","wilson","1234");
		try {
			p.init();
		} catch (LoginFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			List<DIAGasMessage> list =null;
			try {
				list = p.getGasStatus("06", Kind.bySensorID);
				//list.get(0).getStatus();
				//System.out.println(list.size());
				System.out.println("gas: "+list.get(0).getStatus());
			} catch (LoginFailure e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessageFormatError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				p.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		/*Map<String,Object> testMap = new HashMap<String, Object>();
		testMap.put("11", "test1");
		testMap.put("22", "test2");
		
		System.out.println(String.valueOf(testMap.get("11")));*/
		/*Object object = "abcdefg";
		System.out.println(object);*/
	}

}
