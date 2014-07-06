package br.com.onsoftwares.coletacolaborativa;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import br.com.onsoftwares.coletacolaborativa.MapaFragment.GetTiposEPontosIniciaisAsync;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity {
	
	private ListMenuAdapter adapter;
	private ArrayList<ItemMenu> menuItem = new ArrayList<ItemMenu>();
	private ListView menu;
	
	private Dialog dialog;
	
	
	private DrawerLayout menuLayout;
	private ActionBarDrawerToggle actionBarMenu;
	
	private int currentView = 0;
	
	private String tituloHomeLayout = "Coleta Colaborativa";
	private CharSequence tituloMenuLayout = "Menu";
	
	private MapaFragment mapFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapFragment = new MapaFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mapFragment)
                    .commit();
        }
        
        menu = (ListView) findViewById(R.id.left_drawer);
        menuLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        adapter = new ListMenuAdapter(MainActivity.this, menuItem);
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new MenuClickListener());
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(tituloHomeLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        
        this.actionBarMenu = new ActionBarDrawerToggle(this, 
        		this.menuLayout, 
        		R.drawable.ic_drawer, 
        		R.string.app_name, 
        		R.string.app_name) {

        	@Override
        	public void onDrawerClosed(android.view.View drawerView) {
        		getSupportActionBar().setTitle(tituloHomeLayout);
        	};
        	
        	@Override
        	public void onDrawerOpened(android.view.View drawerView) {
        		menu.setAdapter(adapter);
        		getSupportActionBar().setTitle(tituloMenuLayout);
        	};
        };
        
        this.menuLayout.setDrawerListener(this.actionBarMenu);
        this.actionBarMenu.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	outState.putInt("viewId", this.currentView);
    }

	@Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
    	if(this.actionBarMenu.onOptionsItemSelected(item)) {
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	
    	this.actionBarMenu.onConfigurationChanged(newConfig);
    }
    
    private class MenuClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			switch(pos) {
				case 0:
					if (mapFragment.map.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
						mapFragment.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					else
						mapFragment.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					menuLayout.closeDrawer(Gravity.LEFT);
					break;
				case 1:
					mapFragment.initialize();
					menuLayout.closeDrawer(Gravity.LEFT);
					break;
				default:
					Toast.makeText(MainActivity.this, "Hue", Toast.LENGTH_SHORT).show();
			}
		}
    }
    
    // Botão de selecionar tipo
    public void selectTipo (View v) {
    	dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.popup_tipos);
		dialog.setTitle("Selecionar coleta");
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    
	    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    lp.height = (int) (320 * getResources().getDisplayMetrics().density);;
	    
	    dialog.getWindow().setAttributes(lp);
	    
		ListView listTipos = (ListView) dialog.findViewById(R.id.popup_list_tipos);
		ListTiposAdapter adapter = new ListTiposAdapter(MainActivity.this, Global.TIPOS);
		listTipos.setAdapter(adapter);
		
		listTipos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(MainActivity.this, "Pegar pontos to tipo " + Global.TIPOS.get(position), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				new GetPontosDeTipoAsync().execute(Global.TIPOS.get(position).getId(), Global.myLocation.getPosition().latitude + "", Global.myLocation.getPosition().longitude + "");
			}
		});
		dialog.show();
		
	}
    
    // AsyncTask para os pontos de descarte de acordo com opção do usuário
	private class GetPontosDeTipoAsync extends AsyncTask<String, Void, Integer> {
		
		private JSONObject json;
		
		@Override
		protected Integer doInBackground (String... params) {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		   
			if (networkInfo != null && networkInfo.isConnected()) {
		        try {
		        	URL url = new URL(Global.API_URL + "/consulta_ponto_proximo_tipo/");
		        	String urlParams = "tipo_id=" + params[0] + "&latitude=" + params[1] + "&longitude=" + params[2];
		    	    json = Global.HttpRequest(urlParams, url);
		    	    
		    	    return json.getInt("response");
		        } catch (Exception e) {
		        	Log.e("GetPontosDeTipoAsync", e.toString());
		        	return -1;
		        }
		    } else {
		    	return -3;
		    }
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (result != 1)
				Toast.makeText(MainActivity.this, "Ocorreu um erro ao conectar", Toast.LENGTH_SHORT).show();
			else {
				try {
					JSONObject ponto = json.getJSONObject("ponto");
					JSONArray tipos = ponto.getJSONArray("tipos");
					
					mapFragment.map.clear();
					mapFragment.markersOnMap.clear();
					mapFragment.markersOnMapId.clear();
					mapFragment.addUserMarker();
					
					String markerTitle = "";
					ArrayList<ItemTipo> tiposArray = new ArrayList<ItemTipo>();
					for (int i = 0; i < tipos.length(); i++) {
						markerTitle += tipos.getJSONObject(i).getString("nome");
						tiposArray.add(new ItemTipo(tipos.getJSONObject(i).getString("id"), tipos.getJSONObject(i).getString("nome")));
						if (i != tipos.length() - 1)
							markerTitle += ", ";
					}
					
					Marker marker = mapFragment.map.addMarker(new MarkerOptions()
		    				.position(new LatLng(Double.parseDouble(ponto.getString("latitude")), Double.parseDouble(ponto.getString("longitude"))))
		    				.title(markerTitle)
		    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ico)));
					
					String pontoId = ponto.getString("id");
					String descricao = ponto.getString("descricao");
					boolean privado = ponto.getBoolean("ponto_privado");
					JSONObject usuario = ponto.getJSONObject("usuario");
					String nome = usuario.getString("nome");
					String email = usuario.getString("email");
					
					
					
					mapFragment.markersOnMap.add(new ColetaMarker(pontoId, tiposArray, descricao, privado, email, nome, marker.getPosition().latitude, marker.getPosition().longitude));
					mapFragment.markersOnMapId.put(marker.getId(), 0);
					
					// Alterando evento
					Log.d("ID", marker.getId());
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
