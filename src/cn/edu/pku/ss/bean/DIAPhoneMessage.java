package cn.edu.pku.ss.bean;

import java.util.Map;

public class DIAPhoneMessage extends DIAMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4715321712576609315L;
	public static final String VALUE_NAME ="phoneInfo";
	//String phoneInfo;
	public DIAPhoneMessage(String createTime, String diveiceID,
			String sensorName, int eventTimeOut, String eventType,
			String getwayID, String location, String messageID, String occurTime,
			int priority, String sensorID, String topic, String vension,String value) {
		super(createTime, diveiceID, sensorName, eventTimeOut, eventType, getwayID,
				location, messageID, occurTime, priority, sensorID, topic, vension);
		// TODO Auto-generated constructor stub
		//this.phoneInfo = value;
		this.putValueToExtendElements(DIAPhoneMessage.VALUE_NAME, value);
	}
	public DIAPhoneMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DIAPhoneMessage(Map<String,Object>map) {
		this((String)map.get(DIAMessage.CREATE_TIME),
				(String)map.get(DIAMessage.DEVICE_ID),
				(String)map.get(DIAMessage.SENSOR_NAME),
				new Integer(map.get(DIAMessage.EVENT_TIMEOUT).toString()).intValue(),
				(String)map.get(DIAMessage.EVENT_TYPE),
				(String)map.get(DIAMessage.GATEWAY),
				(String)map.get(DIAMessage.LOCATION),
				(String)map.get(DIAMessage.MESSAGE_ID),
				(String)map.get(DIAMessage.OCCUR_TIME),
				new Integer(map.get(DIAMessage.PRIORITY).toString()).intValue(),
				(String)map.get(DIAMessage.SENSOR_ID),
				(String)map.get(DIAMessage.TOPIC),
				(String)map.get(DIAMessage.VERSION),
				(String)map.get(DIAPhoneMessage.VALUE_NAME));
		//System.out.println(">>>value form map:"+(String)map.get(DIAPhoneMessage.VALUE_NAME)+"<<<");
	}
	public String getStatus() {
		/*if(gasStatus.equals("0001"))
			return "high";
		if(gasStatus.equals("0000"))
			return "normal";
		return "invaild data!";*/
		return (String)this.getValueFromExtendElements(DIAPhoneMessage.VALUE_NAME);
		//return phoneInfo;
	}
	
}
