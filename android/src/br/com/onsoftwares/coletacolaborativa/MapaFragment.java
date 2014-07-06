package br.com.onsoftwares.coletacolaborativa;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

 public class MapaFragment extends Fragment {

    	public GoogleMap map;
    	private Context context;
    	
    	private RelativeLayout relativeLayout;
    	
    	private PopupWindow popup;
    	
    	private LocationManager locationManager;
    	private Location location;

    	private double latitude = 0;
    	private double longitude = 0; 
    	
    	private boolean locationByGPSActive;
    	private boolean locationByNetworkActive;
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		context = getActivity().getApplicationContext();
    		
    		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    		
    		relativeLayout = (RelativeLayout) rootView.findViewById(R.id.map_fragment_relative_layout);
    		
    		map = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		
    		locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);

    		locationByGPSActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    		locationByNetworkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    		
    		LocationListener mlocListener = new MyLocationListener();
    		
    		if(locationByGPSActive) {
    			if(locationManager != null) {
    				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, mlocListener);
    				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    				
    				latitude = location.getLatitude();
    				longitude = location.getLongitude();
    				
    				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
    			}
    		}
    		
    		
    		Global.myLocation = this.map.addMarker(new MarkerOptions().
    				position(new LatLng(latitude, longitude))
    				.title("Minha Localização")
    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
    		
    		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener () {
				@Override
				public void onInfoWindowClick(Marker arg0) {
					Toast.makeText(context, "HUE", Toast.LENGTH_SHORT).show();
					
				}
   
    		});
    		
    		new GetTiposAsync().execute("");
    		
    		return rootView;
    	}
    	
    	public class MyLocationListener implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				Global.myLocation.setPosition(new LatLng(latitude, longitude));
				
			}
	
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
    	}
    	
    	// AsyncTask para pegar os tipos de descartes
    	private class GetTiposAsync extends AsyncTask<String, Void, Integer> {
    		
    		@Override
    		protected Integer doInBackground (String... params) {
    			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		   
    			if (networkInfo != null && networkInfo.isConnected()) {
    		        try {
    		        	URL url = new URL(Global.API_URL + "/consulta_tipos/");
    		    	    JSONObject json = Global.HttpRequest(params[0], url);
    		    	    
    		    	    JSONArray tipos = (JSONArray) json.get("tipos");
    		    	    
    		    	    for (int i = 0; i < tipos.length(); i++) {
    		    	    	Global.TIPOS.add(new ItemTipo(tipos.getJSONObject(i).getInt("id") + "", tipos.getJSONObject(i).getString("nome")));
    		    	    }
    		    	    
    		    	    return json.getInt("response");
    		        } catch (Exception e) {
    		        	Log.e("GetTiposAsync", e.toString());
    		        	return -1;
    		        }
    		    } else {
    		    	return -3;
    		    }
    		}
    		
    		@Override
    		protected void onPostExecute(Integer result) {
    			if (result != 1)
    				Toast.makeText(context, "Ocorreu um erro ao conectar", Toast.LENGTH_SHORT).show();
    		}
    	}
    	
    }