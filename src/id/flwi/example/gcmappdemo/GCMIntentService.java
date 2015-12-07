package id.flwi.example.gcmappdemo;

import id.flwi.util.ActivityUtil;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService(){
		super(MainActivity.GCM_SENDER_ID);
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.i("LOG", "got error: " + errorId);
	}

	private int count = 0;
	@SuppressWarnings("deprecation")
	@Override
	protected void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification note = new Notification(R.drawable.ic_launcher, getResources().getString(R.string.app_name), System.currentTimeMillis());
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		note.setLatestEventInfo(context, getResources().getString(R.string.app_name), message, pendingIntent);
		note.number = count++;
		note.defaults |= Notification.DEFAULT_SOUND;
		note.defaults |= Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_LIGHTS;
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, note);		
		//Toast.makeText(GCMIntentService.this, "Button Clicked", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onRegistered(Context context, String regId) {
		Log.i("GCM LOG", "registering " + regId);
		APIWrapper.registerDevice(regId);
		ActivityUtil.setSharedPreference(context, "SHAREDPREF-GCM-REGID", regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.i("GCM LOG", "unregistering " + regId);
		APIWrapper.unregisterDevice(regId);
		ActivityUtil.setSharedPreference(context, "SHAREDPREF-GCM-REGID", "");
	}

}
