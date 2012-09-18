package cn.edu.pku.ss.bean;

import java.io.Serializable;
import java.util.Map;

public class DIAMessage  implements Serializable {
	/**
	 * 
	 */
	
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String GATEWAY = "GATEWAY";
	public static final String SENSOR_ID = "SENSOR_ID";
	public static final String MESSAGE_ID = "MESSAGE_ID";
	public static final String DEVICE_ID = "DEVICE_ID";
	public static final String VERSION = "VERSION";
	public static final String TOPIC = "TOPIC";
	public static final String EVENT_TYPE = "EVENT_TYPE";
	public static final String PRIORITY = "PRIORITY";
	public static final String LOCATION = "LOCATION";
	public static final String OCCUR_TIME = "OCCUR_TIME";
	public static final String EVENT_TIMEOUT = "EVENT_TIMEOUT";
	public static final String SENSOR_NAME = "SENSOR_NAME";
	
	private static final long serialVersionUID = 1L;
	private String createTime;
	private String gateway;
	private String sensorID;
	private String messageID;
	private String deviceID;
	private String vension;
	private String topic;
	private String eventType;
	private int priority;
	private String location;
	private String occurTime;
	private int eventTimeOut;
	private String sensorName;
	private Map<String,Object> extendElements = new java.util.concurrent.ConcurrentHashMap<String, Object>();

	public DIAMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DIAMessage(String createTime, String deviceID, String sensorName,
			int eventTimeOut, String eventType, String gateway,
			String location, String messageID, String occurTime, int priority,
			String sensorID, String topic, String vension) {
		super();
		this.createTime = createTime;
		this.deviceID = deviceID;
		this.sensorName = sensorName;
		this.eventTimeOut = eventTimeOut;
		this.eventType = eventType;
		this.gateway = gateway;
		this.location = location;
		this.messageID = messageID;
		this.occurTime = occurTime;
		this.priority = priority;
		this.sensorID = sensorID;
		this.topic = topic;
		this.vension = vension;
	}
	public DIAMessage(String createTime, String deviceID, String sensorName,
			int eventTimeOut, String eventType, String getway,
			String location, String messageID, String occurTime, int priority,
			String sensorID, String topic, String vension, Map<String, Object>extendElements) {
		super();
		this.createTime = createTime;
		this.deviceID = deviceID;
		this.sensorName = sensorName;
		this.eventTimeOut = eventTimeOut;
		this.eventType = eventType;
		this.gateway = getway;
		this.location = location;
		this.messageID = messageID;
		this.occurTime = occurTime;
		this.priority = priority;
		this.sensorID = sensorID;
		this.topic = topic;
		this.vension = vension;
		this.extendElements = extendElements;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	public String getSensorName() {
		return sensorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getGateway() {
		return gateway;
	}
	public String getSensorID() {
		return sensorID;
	}
	public String getMessageID() {
		return messageID;
	}
	public String getVension() {
		return vension;
	}
	public String getTopic() {
		return topic;
	}
	public String getEventType() {
		return eventType;
	}
	public int getPriority() {
		return priority;
	}
	public String getLocation() {
		return location;
	}
	public String getOccurTime() {
		return occurTime;
	}
	public int getEventTimeOut() {
		return eventTimeOut;
	}
	public Map<String,Object> getExtendElements() {
		return extendElements;
	}
	
	/**
	 * get value from extendElements
	 * return Object
	 */
	public Object getValueFromExtendElements(String key)
	{
		
		return this.extendElements.get(key);
	}
	
	/**
	 * put value to extendElements
	 */
	public void putValueToExtendElements(String key, Object value)
	{
		this.extendElements.put(key, value);
	}

	
}
