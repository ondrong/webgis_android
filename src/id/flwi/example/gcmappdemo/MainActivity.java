package id.flwi.example.gcmappdemo;


import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements OnItemClickListener {
	String data;
	String idp,hp,userId;
	TextView txtHp;
	String ip = "http://192.168.43.88";
	
	List<Item> arrayOfList;
	List<Item> arrayOfList2;
	ListView listView;
	ListView lisTorderStatus;
	NewsRowAdapter objAdapter;
	NewsRowAdapter objAdapterStatus;
	
	public static String GCM_SENDER_ID = "1026002273953"; //id dari google	
	
	private static final String rssFeed = "http://192.168.43.88/webgis/android_return.php?action=cekOrder&userid=357671031485632";

	private static final String ARRAY_NAME = "penumpang";
	private static final String ID = "id";
	private static final String NAME = "nama";
	private static final String CITY = "tempat";
	private static final String AGE = "nohp";
	private static final String IDPENGGUNA ="idp";
	
	private static final String rssFeed2 = "http://192.168.43.88/webgis/android_return.php?action=orderTerima&userid=357671031485632";

	private static final String ARRAY_NAME2 = "penumpang";
	private static final String ID2 = "id";
	private static final String NAMA = "nama";
	private static final String TEMPAT = "tempat";
	private static final String NOHP = "nohp";
	private static final String IDPENGGUNA2 ="idp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                     		
		listView = (ListView) findViewById(R.id.listOrder);
		listView.setOnItemClickListener(this);
		
		lisTorderStatus = (ListView) findViewById(R.id.listTerima);
		//lisTorderStatus.setOnItemClickListener(this);
						
		arrayOfList = new ArrayList<Item>();
		
		arrayOfList2 = new ArrayList<Item>();
		
		if (Utils.isNetworkAvailable(MainActivity.this)) {
			//list Status Order Antrian
			new MyTask().execute(rssFeed);
			//list Status Terima Dan Tolak Order
			new OrderTask().execute(rssFeed2);
		} else {
			showToast("No Network Connection!!!");
		}
		
		
    }
    
    public void register(){
    	GCMRegistrar.checkDevice(getApplicationContext());
		GCMRegistrar.checkManifest(getApplicationContext());
		final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
		
		AlertDialog.Builder peringatan = new AlertDialog.Builder(MainActivity.this);
		peringatan.setTitle("Pemberitahuan");
		if (regId.equals("")) {
		    GCMRegistrar.register(getApplicationContext(), GCM_SENDER_ID);
			peringatan.setMessage("Telah Didaftar");
			peringatan.setNeutralButton("OK", null);
			peringatan.show();
		} 
		else {
			peringatan.setMessage("Anda Telah Terdaftar Sebagai User");
			peringatan.setNeutralButton("OK", null);
			peringatan.show();
		}
    }
    
    public void unregister(){
    	GCMRegistrar.checkDevice(getApplicationContext());
		GCMRegistrar.checkManifest(getApplicationContext());
		final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
		AlertDialog.Builder peringatan = new AlertDialog.Builder(MainActivity.this);
		peringatan.setTitle("Pemberitahuan");
		if (!regId.equals("")) {
			GCMRegistrar.unregister(getApplicationContext());
			peringatan.setMessage("User Dihapus");
			peringatan.setNeutralButton("OK", null);
			peringatan.show();
		} else {
			peringatan.setMessage("Anda Belum Terdaftar");
			peringatan.setNeutralButton("OK", null);
			peringatan.show();
		}
    }
    
    
    private class actionOrderTerima  extends AsyncTask<String, Void, Void> {
    	
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;        
        protected void onPreExecute() {
        	
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
                
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }
            
            return null;
        }
        
        protected void onPostExecute(Void unused) {            
            if (Error != null) {
                                
            } 
            else {            	
            	String phoneNo= hp;
            	kirimSms("Pesanan Order anda telah diterima dimohon untuk menunggu konfirmasi selanjutnya", phoneNo, "SMS Terkirim, Order Diterima");
            	
             }
        }
        
    }
  
private class actionOrderTOlak  extends AsyncTask<String, Void, Void> {
    	
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;        
        protected void onPreExecute() {
        	
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
                
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }
            
            return null;
        }
        
        protected void onPostExecute(Void unused) {            
            if (Error != null) {
                                
            } 
            else {         	
            	String phoneNo = hp;
            	kirimSms("Maaf Pesanan Ditolak berhubung supir sementara mendapatkan order atau tidak dalam masa kerja", phoneNo, "SMS Dikirim, Order Ditolak");
             }
        }
        
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        //return true;
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.activity_main, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
         case R.id.action_regis:
             register();
             return true;
         case R.id.action_unreg:
             // location found
         	 unregister();
             return true;
             
         case R.id.gps:
             // location found
         	 bukaGps();
             return true;
         default:
            return super.onOptionsItemSelected(item); 
         }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Log.i("Tes", view.toString());
		showDeleteDialog(position);
	}
	
	
	private void showDeleteDialog(final int position) {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle("Now Order");
		alertDialog.setMessage("Tentukan Pilihan Anda Untuk Menerima Atau Menolak?");
		Item daftarObjek = arrayOfList.get(position);
		hp = daftarObjek.getAge();
		userId = daftarObjek.getId();
		idp = daftarObjek.getIDP();	
		alertDialog.setButton("Tolak Order", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				arrayOfList.remove(position);
				objAdapter.notifyDataSetChanged();
				String serverURL = ip+"/webgis/android_return.php?action=tolakOrder&idpengguna="+idp+"&userid="+userId; 
				new actionOrderTOlak().execute(serverURL);
			}
		});
		alertDialog.setButton2("Terima Order", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				arrayOfList.remove(position);
				objAdapter.notifyDataSetChanged();
				String serverURL = ip+"/webgis/android_return.php?action=terimaOrder&idpengguna="+idp+"&userid="+userId; 
				new actionOrderTerima().execute(serverURL);
			}
		});
		alertDialog.show();

	}

	public void setAdapterToListview() {
		objAdapter = new NewsRowAdapter(MainActivity.this, R.layout.row,arrayOfList);
		objAdapterStatus = new NewsRowAdapter(MainActivity.this, R.layout.rowterima, arrayOfList2);
		listView.setAdapter(objAdapter);
		lisTorderStatus.setAdapter(objAdapterStatus);
	}

	public void showToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	class MyTask extends AsyncTask<String, Void, String> {
		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Dialog.setMessage("Menghubungkan");
            Dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0) {
				showToast("Tidak Ada Data Order");
				Dialog.dismiss();
			} else {
				Dialog.dismiss();
				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);
						Item objItem = new Item();
						objItem.setId(objJson.getString(ID));
						objItem.setName(objJson.getString(NAME));
						objItem.setCity(objJson.getString(CITY));
						objItem.setAge(objJson.getString(AGE));
						objItem.setIDP(objJson.getString(IDPENGGUNA));				
						arrayOfList.add(objItem);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Collections.sort(arrayOfList, new Comparator<Item>() {

					@Override
					public int compare(Item lhs, Item rhs) {
						return 0;
					}
				});
				
				setAdapterToListview();

			}

		}
	}
	
	class OrderTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0) {
				showToast("Tidak Ada Data Order Diterima Dan Ditolak");
			} else {
				try {
					JSONObject mainJson1 = new JSONObject(result);
					JSONArray jsonArray1 = mainJson1.getJSONArray(ARRAY_NAME2);
					for (int i = 0; i < jsonArray1.length(); i++) {
						JSONObject objJson1 = jsonArray1.getJSONObject(i);
						Item objItem1 = new Item();
						objItem1.setIdMobil(objJson1.getString(ID2));
						objItem1.setNama(objJson1.getString(NAMA));
						objItem1.setTempat(objJson1.getString(TEMPAT));
						objItem1.setHp(objJson1.getString(NOHP));
						objItem1.setIdPengguna(objJson1.getString(IDPENGGUNA2));				
						arrayOfList2.add(objItem1);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Collections.sort(arrayOfList2, new Comparator<Item>() {

					@Override
					public int compare(Item lhs, Item rhs) {
						return 0;
					}
				});
				setAdapterToListview();
			}

		}
	}

    public void bukaGps(){
    	Intent gps = new Intent(this, GpsActivity.class); 
        startActivity(gps);
    }
    
    public void kirimSms(String sms,String phoneNo,String status){
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, sms, null, null);
			Toast.makeText(getApplicationContext(), status+" Ke : "+phoneNo,Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),"SMS Tidak Terkirim...",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
    }
    
}
