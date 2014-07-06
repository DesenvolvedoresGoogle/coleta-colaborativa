package br.com.onsoftwares.coletacolaborativa;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
    	public ArrayList<ColetaMarker> markersOnMap = new ArrayList<ColetaMarker>();
    	public HashMap<String, Integer> markersOnMapId = new HashMap<String, Integer>();
    	
    	private Context context;
    	
    	private RelativeLayout relativeLayout;
    	
    	private PopupWindow popup;
    	
    	private LocationManager locationManager;
    	private LocationListener mlocListener;
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
    		
    		mlocListener = new MyLocationListener();
    		
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
				public void onInfoWindowClick(Marker marker) {
					if (!marker.equals(Global.myLocation)) {
						Dialog dialog = new Dialog(getActivity());
						dialog.setContentView(R.layout.dialog_marker);
						dialog.setTitle("Ponto de coleta");
						
						final ColetaMarker m = markersOnMap.get(markersOnMapId.get(marker.getId()));
						
						TextView tiposTextView = (TextView) dialog.findViewById(R.id.dialog_marker_tipos);
						tiposTextView.setText(m.getTipos());
						
						TextView descricaoTextView = (TextView) dialog.findViewById(R.id.dialog_marker_desc);
						descricaoTextView.setText(m.getDescricao());
						
						if (m.isPrivado()) {
							TableRow rowPrivado = (TableRow) dialog.findViewById(R.id.dialog_marker_private);
							rowPrivado.setVisibility(View.VISIBLE);
							
							TableRow rowUsuario = (TableRow) dialog.findViewById(R.id.dialog_marker_usuario_row);
							rowUsuario.setVisibility(View.VISIBLE);
							
							TableRow rowEmail = (TableRow) dialog.findViewById(R.id.dialog_marker_email_row);
							rowEmail.setVisibility(View.VISIBLE);
							
							TextView usuarioTextView = (TextView) dialog.findViewById(R.id.dialog_marker_usuario);
							usuarioTextView.setText(m.getNomeUsuario());
							
							Log.d("NOME", m.getNomeUsuario());
							
							TextView emailTextView = (TextView) dialog.findViewById(R.id.dialog_marker_email);
							emailTextView.setText(m.getEmail());
						}
						
						Button confirmarDescarte = (Button) dialog.findViewById(R.id.dialog_marker_confirmar_descarte);
						confirmarDescarte.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(context, m.getLatitude() + " - " + m.getLongitude() , Toast.LENGTH_SHORT).show();
							}
						});
						
						dialog.show();
					}
					
				}
   
    		});
    		
    		new GetTiposEPontosIniciaisAsync().execute(Global.myLocation.getPosition().latitude + "", Global.myLocation.getPosition().longitude + "");
    		
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
    	private class GetTiposEPontosIniciaisAsync extends AsyncTask<String, Void, Integer> {
    		
    		private JSONObject jsonPontos;
    		
    		@Override
    		protected Integer doInBackground (String... params) {
    			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		   
    			if (networkInfo != null && networkInfo.isConnected()) {
    		        try {
    		        	URL url1 = new URL(Global.API_URL + "/consulta_tipos/");
    		        	URL url2 = new URL(Global.API_URL + "/consulta_ponto_proximo/");
    		    	    JSONObject jsonTipos = Global.HttpRequest(params[0], url1);
    		    	    
    		    	    String pontosParams = "latitude=" + params[0] + "&longitude=" + params[1];
    		    	    jsonPontos = Global.HttpRequest(pontosParams, url2);
    		    	    
    		    	    JSONArray tipos = (JSONArray) jsonTipos.get("tipos");
    		    	    
    		    	    for (int i = 0; i < tipos.length(); i++) {
    		    	    	Global.TIPOS.add(new ItemTipo(tipos.getJSONObject(i).getInt("id") + "", tipos.getJSONObject(i).getString("nome")));
    		    	    }
    		    	    
    		    	    return jsonTipos.getInt("response");
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
    			else {
    				
    				// Adicionando marcadores
    				JSONArray pontos;
					try {
						pontos = jsonPontos.getJSONArray("pontos");
		    	    
			    	    for (int i = 0; i < pontos.length(); i++) {
			    	    	JSONArray tiposPonto = pontos.getJSONObject(i).getJSONArray("tipos");
			    	    	String markerTitle = "";
							for (int j = 0; j < tiposPonto.length(); j++) {
								markerTitle += tiposPonto.getJSONObject(j).getString("nome");
								if (j != tiposPonto.length() - 1)
									markerTitle += ", ";
							}
							
							Marker marker = map.addMarker(new MarkerOptions()
			    				.position(new LatLng(Double.parseDouble(pontos.getJSONObject(i).getString("latitude")), 
			    						Double.parseDouble(pontos.getJSONObject(i).getString("longitude"))))
			    				.title(markerTitle)
			    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
			    	    	
			    	    	String descricao = pontos.getJSONObject(i).getString("descricao");
							boolean privado = pontos.getJSONObject(i).getBoolean("ponto_privado");
							JSONObject usuario = pontos.getJSONObject(i).getJSONObject("usuario");
							String nome = usuario.getString("nome");
							String email = usuario.getString("email");
							
							markersOnMap.add(new ColetaMarker(markerTitle, descricao, privado, email, nome, marker.getPosition().latitude, marker.getPosition().longitude));
							markersOnMapId.put(marker.getId(), i);
			    	    }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    			}
    		}
    	}
    	
    	public  void addUserMarker () {
    		if(locationByGPSActive) {
    			if(locationManager != null) {
    				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, mlocListener);
    				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    				
    				latitude = location.getLatitude();
    				longitude = location.getLongitude();
    				
    				Global.myLocation = this.map.addMarker(new MarkerOptions().
    	    				position(new LatLng(latitude, longitude))
    	    				.title("Minha Localização")
    	    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
    			}
    			
    		}
    	}
    	
    }