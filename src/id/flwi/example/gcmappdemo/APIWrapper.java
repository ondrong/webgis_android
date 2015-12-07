package id.flwi.example.gcmappdemo;

import id.flwi.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class APIWrapper {
	public static boolean registerDevice(String regId) {
		String idMobil= "357671031485632";
		boolean response = false;
		String result = "";
		
		Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("idMobil", idMobil);
        
        try {
        	//FIXME: change it to real API_URL
        	result = HttpUtil.post("http://103.245.72.45/webgis/register_gcm.php", params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        if(result.equalsIgnoreCase("OK")){
        	response = true;
        }
		return response;
	}
	
	public static boolean unregisterDevice(String regId){
		boolean response = false;
		String result = "";
		
		Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        try {
        	//FIXME: change it to real API_URL
        	result = HttpUtil.post("http://103.245.72.45/webgis/unregister_gcm.php", params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        if(result.equalsIgnoreCase("OK")){
        	response = true;
        }
		return response;
	}

}
