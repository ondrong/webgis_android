package id.flwi.util;

/**
 * @author Arief Bayu Purwanto
 */
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class ActivityUtil {
	public static final String PREFS_NAME = "____MyPrefsFile";
	
	public static void debugExtra(Bundle extras){
		if( extras != null) {
			Log.i("Log", "printing all extras information");
			java.util.Set<String> keys = extras.keySet();
			java.util.Iterator<String> keyIterator = keys.iterator();
			int index = 0;
			while(keyIterator.hasNext()) {
				Log.i("log", "  extras #" + (++index) + ": " + keyIterator.next());
			}
		} else {
			Log.i("Log", "empty extras");
		}
	}
	
	public static boolean getSharedPreferenceBoolean(Context c, String preference){
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    return settings.getBoolean(preference, false);
	}
	public static boolean getSharedPreferenceBoolean(Context c, String preference, boolean defaultValue){
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    return settings.getBoolean(preference, defaultValue);
	}
	public static String getSharedPreferenceString(Context c, String preference){
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    return settings.getString(preference, "");
	}
	public static String getSharedPreferenceString(Context c, String preference, String defaultValue){
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    return settings.getString(preference, defaultValue);
	}
	
	public static void setSharedPreference(Context c, String preference, boolean prefValue){
		
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(preference, prefValue);
	    editor.commit();
	}
	public static void setSharedPreference(Context c, String preference, String prefValue){
		SharedPreferences settings = c.getSharedPreferences(ActivityUtil.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(preference, prefValue);
	    editor.commit();
	}
	
	
	
	public static Bitmap getInternalBitmap(Context c, String filename){
		Bitmap b = null;
		try {
			FileInputStream fis = c.openFileInput(filename);
			BufferedInputStream buf = new BufferedInputStream(fis);
	        b = BitmapFactory.decodeStream(buf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public static void setInternalBitmap(Context c, String filename, Bitmap bitmap){
		
		try {
			FileOutputStream fos = c.openFileOutput(filename, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
