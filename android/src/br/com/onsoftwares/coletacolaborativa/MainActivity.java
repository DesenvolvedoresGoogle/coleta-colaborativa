package br.com.onsoftwares.coletacolaborativa;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	private ListMenuAdapter adapter;
	private ArrayList<ItemMenu> menuItem = new ArrayList<ItemMenu>();
	private ListView menu;
	
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
				default:
					Toast.makeText(MainActivity.this, "Hue", Toast.LENGTH_SHORT).show();
			}
		}
    }
}
