package cn.edu.pku.ss.eventProcessor;

import java.util.Map;

import dk.itu.infobus.ws.Util;

public interface DIAListener {
	public void cleanUp() throws Exception;
    public void onStarted() ;
    public void onMessage(Map<String, String> msg);
}
