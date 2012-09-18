package cn.edu.pku.ss.bean;

import java.util.Map;

public class DIAOtherMessage extends DIAMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4715321712576609315L;
	
	public static final String VALUE_NAME = "Other";
	
	public DIAOtherMessage(String createTime, String diveiceID,
			String sensorName, int eventTimeOut, String eventType,
			String getwayID, String location, String messageID, String occurTime,
			int priority, String sensorID, String topic, String vension,double value) {
		super(createTime, diveiceID, sensorName, eventTimeOut, eventType, getwayID,
				location, messageID, occurTime, priority, sensorID, topic, vension);
		// TODO Auto-generated constructor stub
		this.putValueToExtendElements(DIAOtherMessage.VALUE_NAME, new Double(value));
	}
	public DIAOtherMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DIAOtherMessage(Map<String,Object>map) {
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
				new Double(map.get(DIAOtherMessage.VALUE_NAME).toString()).doubleValue());
	}
	public Double getStatus() {
		return (Double)this.getValueFromExtendElements(DIAOtherMessage.VALUE_NAME);
	}
	
}
