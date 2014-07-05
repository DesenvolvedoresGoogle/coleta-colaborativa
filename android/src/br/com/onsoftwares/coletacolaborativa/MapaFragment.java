package br.com.onsoftwares.coletacolaborativa;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

 public class MapaFragment extends Fragment {

    	private GoogleMap map;
    	private Context context;
    	private Marker myLocation;
    	
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
    		
    		
    		myLocation = this.map.addMarker(new MarkerOptions().
    				position(new LatLng(latitude, longitude))
    				.title("Minha Localização")
    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
    				
    		return rootView;
    	}
    	
    	public class MyLocationListener implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				myLocation.setPosition(new LatLng(latitude, longitude));
				
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
    }