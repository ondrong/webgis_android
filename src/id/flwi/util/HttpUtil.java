package id.flwi.util;
/**
 * @author Arief Bayu Purwanto
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

public class HttpUtil {
	private static String CHARSET = "UTF-8";
	public static String getRequestParameter(String bounrdy, String key,
			String value) {
		return _getRequestParameterImpl(bounrdy, key, value);
	}

	public static String getRequestParameter(String bounrdy, String key,
			int value) {
		return _getRequestParameterImpl(bounrdy, key, String.valueOf(value));
	}

	public static String _getRequestParameterImpl(String bounrdy, String key,
			String value) {
		StringBuilder requestBody = new StringBuilder();
		requestBody.append("--");
		requestBody.append(bounrdy);
		requestBody.append("\r\n");
		requestBody.append("Content-Disposition: form-data; name=\"" + key
				+ "\"");
		requestBody.append("\r\n");
		requestBody.append("\r\n");
		requestBody.append(value);
		requestBody.append("\r\n");

		return requestBody.toString();
	}
	
	public static String get(String url) throws IOException{
		URL connectURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection(); 
		conn.setRequestProperty("Content-Type", "text/plain; charset=" + CHARSET.toLowerCase());
		conn.setRequestProperty("Accept-Charset", CHARSET);

		// do some setup
		conn.setDoInput(true); 
		conn.setDoOutput(true); 
		conn.setUseCaches(false); 
		conn.setRequestMethod("GET");

		// connect and flush the request out
		conn.connect();
		conn.getOutputStream().flush();

		return getHTTPResponse(conn);
	}
	
	public static String post(String url, Map<String, String> parameters) throws IOException{
		URL connectURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection(); 
		
		String query = buildQueries(parameters);
		Log.i("LOG", "doing query: " + query);
		
		conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
		conn.setRequestProperty("Content-Length", "" + 
	               Integer.toString(query.getBytes().length));

		// do some setup
		conn.setDoInput(true); 
		conn.setDoOutput(true); 
		conn.setUseCaches(false); 
		conn.setConnectTimeout(30 * 1000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", 
        "application/x-www-form-urlencoded");
		
		DataOutputStream output = null;
		try {
		     output = new DataOutputStream (conn.getOutputStream ());
		     output.writeBytes(query);
		     output.flush();
		} finally {
		     if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
		}
		
//		conn.connect();

		return getHTTPResponse(conn);
	}
	
	private static String buildQueries(Map<String, String> parameters) {
		StringBuilder sb = new StringBuilder();
		
		for (String key: parameters.keySet()) {
			sb.append(key);
			sb.append("=");
			try {
				sb.append(URLEncoder.encode(parameters.get(key), CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&");
        }
		//Log.i("LOG", "parameter: " + sb.toString());
		return sb.toString();
	}
	
	private static String getHTTPResponse(HttpURLConnection conn){
		StringBuilder strB = new StringBuilder();
		try {
		    BufferedReader input = new BufferedReader(
		            new InputStreamReader(conn.getInputStream(), "UTF-8"), 16384);//double the default buffer size
		    String str;
		    while (null != (str = input.readLine())) {
		        strB.append(str).append("\r\n"); 
		    }
		    input.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return strB.toString();
	}

	/*private static String getHTTPResponse(HttpURLConnection conn)
	{
	    InputStream is = null;
	    try 
	    {
	        is = conn.getInputStream(); 
	        // scoop up the reply from the server
	        int ch; 
	        StringBuffer sb = new StringBuffer(); 
	        while( ( ch = is.read() ) != -1 ) { 
	            sb.append( (char)ch ); 
	        } 
	        return sb.toString(); 
	    }
	    catch(Exception e)
	    {
	       Log.e("HTTPUTIL", "biffed it getting HTTPResponse");
	    }
	    finally 
	    {
	        try {
	        if (is != null)
	            is.close();
	        } catch (Exception e) {}
	    }

	    return "";
	}*/

}
