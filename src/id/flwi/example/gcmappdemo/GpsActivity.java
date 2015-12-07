package id.flwi.example.gcmappdemo;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GpsActivity extends Activity {
	
	GpsService	gps;
	Button btnUpdate;
	Button btnCariLokasi;
	EditText noPol;
	EditText namaSupir;
	EditText latid;
	EditText longit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnCariLokasi = (Button) findViewById(R.id.btnCariLokasi);
		
		noPol = (EditText) findViewById(R.id.editNopol);
		namaSupir = (EditText) findViewById(R.id.editSopir);
		latid = (EditText) findViewById(R.id.editLatitude);
		longit = (EditText) findViewById(R.id.editLongitude);
		
		noPol.setKeyListener(null);
		//namaSupir.setKeyListener(null);
		latid.setKeyListener(null);
		longit.setKeyListener(null);
		
		btnCariLokasi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				gpsAct();
			}
		});
		
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String plat = noPol.getText().toString();
				String lat=latid.getText().toString();
				String longits =longit.getText().toString();
				
				String url="http://103.245.72.45/webgis/uploadLokasi.php?action=updateLokasi&idmobil="+plat+"&latitude="+lat+"&longitude="+longits+"&speed=0&status=AUTOLOW";
				new actionUpload().execute(url);
			}
		});
		
		// buat class object dari GpsService
		gps = new GpsService(GpsActivity.this);

		// dicek dulu apakah GPSnya idup
		if (gps.canGetLocation())
		{
			// ambil latitude dan longitude
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			String isiLat = Double.toString(latitude);
			String isiLong = Double.toString(longitude);
			
			latid.setText(isiLat);
			longit.setText(isiLong);
			noPol.setText("357671031485632");
			namaSupir.setText("iank");
			
		} else
		{
			gps.showSettingAlert();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void gpsAct(){
		gps = new GpsService(GpsActivity.this);

		if (gps.canGetLocation())
		{
			// ambil latitude dan longitude
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			String isiLat = Double.toString(latitude);
			String isiLong = Double.toString(longitude);
			AlertDialog.Builder viewLoc = new AlertDialog.Builder(GpsActivity.this);
			viewLoc.setTitle("Lokasi Anda Saat Ini");
			viewLoc.setMessage("Latitud :"+isiLat+"\n Longitude :"+isiLong);
			viewLoc.setNeutralButton("OK", null);
			viewLoc.show();
		} else
		{
			gps.showSettingAlert();
		}
	}
	
	private class actionUpload  extends AsyncTask<String, Void, Void> {
    	
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
            	Toast.makeText(GpsActivity.this, "Lokasi Berhasil Diupdate", Toast.LENGTH_LONG).show();                
            } 
            else{
            	Toast.makeText(GpsActivity.this, "Lokasi Tidak Berhasil Diupdate", Toast.LENGTH_LONG).show();  
            }
        }
        
    }
}
