package cn.edu.pku.ss.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.edu.pku.ss.eventProcessor.ContinuedEventProcessor;
/**
 * 蓝牙收集类：将蓝牙信息收集到bluetoothList，每隔3秒将收集到的信息进行处理，统一发送给DIAServer，然后清空bluetoothList
 * 优点：可以将多个蓝牙信息集成在一起，做成一个控制命令发送，节约多次发送的时间
 * @author yanangong & qinhuiwang
 *
 */
public class BluetoothCollection {
	public static ContinuedEventProcessor cEventProcessor;//持续性事件处理器
	public static List<String> bluetoothList;//存储蓝牙信息的列表
	public static boolean sendToLoudspeakerOrPhone = false;//用于设置是控制喇叭还是控制手机
	public final static boolean SEND_TO_LOUDSPEAKER = true;
	public final static boolean SEND_TO_PHONE = false;

	/**
	 * 初始化蓝牙列表，并启动一个线程持续监听
	 * @param ep 持续性的处理器
	 */
	public static void init(ContinuedEventProcessor ep) {
		cEventProcessor = ep;
		bluetoothList = Collections.synchronizedList(new ArrayList<String>());//初始化一个线程安全的list
		
		Thread sendAddrsThread = new Thread() {//创建一个线程，用于隔3秒处理一次收集的蓝牙信息
			public void run() {
				String allPhoneAddr = "";
				while (true) {
					allPhoneAddr = "";
					try {
						Thread.sleep(3000);//停顿三秒
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					synchronized (bluetoothList) {//线程的同步锁
						Iterator iter = bluetoothList.iterator();
						String next = "";
						while (iter.hasNext()) {
							next = (String) iter.next();
							if (iter.hasNext()) {
								allPhoneAddr += next + "/";
							} else {
								allPhoneAddr += next;
							}
						}
						bluetoothList.clear();
					}
					if (!allPhoneAddr.equals("")) {
						//System.out.println("allPhone:" + allPhoneAddr);
						try {
							if (sendToLoudspeakerOrPhone) {// 发送命令控制喇叭
								cEventProcessor.sendLoudspeakerCommand(allPhoneAddr);
							} else {// 发送命令来控制手机
								cEventProcessor.sendAudioToPhoneCommand(allPhoneAddr);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		};
		sendAddrsThread.start();//启动线程
	}

	/**
	 * 往蓝牙列表中添加一个蓝牙地址
	 * @param bluetooth 蓝牙地址
	 */
	public static void addBluetooth(String bluetooth) {
		synchronized (bluetoothList) {//线程的同步锁
			bluetoothList.add(bluetooth);
		}
	}

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { BluetoothCollection.init(); int
	 * address = 10000;
	 * 
	 * for(int j=0;j<10;j++){ for(int i=0;i<20;i++){
	 * BluetoothCollection.addBluetooth(String.valueOf(address++)); } try {
	 * Thread.sleep(10000); } catch (InterruptedException e) {
	 * e.printStackTrace(); } }
	 * 
	 * 
	 * }
	 */

}
