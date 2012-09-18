package cn.edu.pku.ss.bean;

import java.math.BigDecimal;
import java.util.Map;

public class DIATemperatureMessage extends DIAMessage {

	public  static final String VALUE_NAME = "Temperature";
	
	public DIATemperatureMessage(String createTime, String diveiceID,
			String sensorName, int eventTimeOut, String eventType,
			String getwayID, String location, String messageID, String occurTime,
			int priority, String sensorID, String topic, String vension,double value) {
		super(createTime, diveiceID, sensorName, eventTimeOut, eventType, getwayID,
				location, messageID, occurTime, priority, sensorID, topic, vension);
		// TODO Auto-generated constructor stub
		this.putValueToExtendElements(DIATemperatureMessage.VALUE_NAME, new Double(value));
	}
	
	public DIATemperatureMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DIATemperatureMessage(Map<String, Object> map) {
		this(
				(String) map.get(DIAMessage.CREATE_TIME), 
				(String) map.get(DIAMessage.DEVICE_ID), 
				(String) map.get(DIAMessage.SENSOR_NAME), 
				new Integer(map.get(DIAMessage.EVENT_TIMEOUT).toString()).intValue(), 
				(String) map.get(DIAMessage.EVENT_TYPE), 
				(String) map.get(DIAMessage.GATEWAY),
				(String) map.get(DIAMessage.LOCATION), 
				(String) map.get(DIAMessage.MESSAGE_ID), 
				(String) map.get(DIAMessage.OCCUR_TIME), 
				new Integer(map.get(DIAMessage.PRIORITY).toString()).intValue(),
				(String) map.get(DIAMessage.SENSOR_ID), 
				(String) map.get(DIAMessage.TOPIC), 
				(String) map.get(DIAMessage.VERSION), 
				new Double(map.get(DIATemperatureMessage.VALUE_NAME).toString()).doubleValue());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double getTemperature() {
		return ((BigDecimal)this.getValueFromExtendElements(DIATemperatureMessage.VALUE_NAME)).doubleValue();
	}
	
}
