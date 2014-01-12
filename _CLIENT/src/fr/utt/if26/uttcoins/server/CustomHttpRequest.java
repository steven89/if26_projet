package fr.utt.if26.uttcoins.server;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface CustomHttpRequest {
	public void loadParams() throws UnsupportedEncodingException;

	public void setParams(Map<String, Object> paramMap);
	
	public void putParam(String key, Object value);
		
	public void clearParams();
}
