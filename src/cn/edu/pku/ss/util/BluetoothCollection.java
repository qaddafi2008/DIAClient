package cn.edu.pku.ss.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.edu.pku.ss.eventProcessor.ContinuedEventProcessor;
/**
 * �����ռ��ࣺ��������Ϣ�ռ���bluetoothList��ÿ��3�뽫�ռ�������Ϣ���д���ͳһ���͸�DIAServer��Ȼ�����bluetoothList
 * �ŵ㣺���Խ����������Ϣ������һ������һ����������ͣ���Լ��η��͵�ʱ��
 * @author yanangong & qinhuiwang
 *
 */
public class BluetoothCollection {
	public static ContinuedEventProcessor cEventProcessor;//�������¼�������
	public static List<String> bluetoothList;//�洢������Ϣ���б�
	public static boolean sendToLoudspeakerOrPhone = false;//���������ǿ������Ȼ��ǿ����ֻ�
	public final static boolean SEND_TO_LOUDSPEAKER = true;
	public final static boolean SEND_TO_PHONE = false;

	/**
	 * ��ʼ�������б�������һ���̳߳�������
	 * @param ep �����ԵĴ�����
	 */
	public static void init(ContinuedEventProcessor ep) {
		cEventProcessor = ep;
		bluetoothList = Collections.synchronizedList(new ArrayList<String>());//��ʼ��һ���̰߳�ȫ��list
		
		Thread sendAddrsThread = new Thread() {//����һ���̣߳����ڸ�3�봦��һ���ռ���������Ϣ
			public void run() {
				String allPhoneAddr = "";
				while (true) {
					allPhoneAddr = "";
					try {
						Thread.sleep(3000);//ͣ������
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					synchronized (bluetoothList) {//�̵߳�ͬ����
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
							if (sendToLoudspeakerOrPhone) {// ���������������
								cEventProcessor.sendLoudspeakerCommand(allPhoneAddr);
							} else {// ���������������ֻ�
								cEventProcessor.sendAudioToPhoneCommand(allPhoneAddr);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		};
		sendAddrsThread.start();//�����߳�
	}

	/**
	 * �������б������һ��������ַ
	 * @param bluetooth ������ַ
	 */
	public static void addBluetooth(String bluetooth) {
		synchronized (bluetoothList) {//�̵߳�ͬ����
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
