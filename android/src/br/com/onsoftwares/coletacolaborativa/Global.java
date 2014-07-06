package br.com.onsoftwares.coletacolaborativa;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Global {
	public static final String API_URL = "http://10.10.10.109:8000";
	
	public static ArrayList<ItemTipo> TIPOS = new ArrayList<ItemTipo>();
	
	public static Marker myLocation;
	
	
	// Método para fazer as requisições HTTP e retornar a resposta JSON
	public static JSONObject HttpRequest(String urlParameters, URL url) throws Exception{
		
		//Connection parameters
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("charset", "utf-8");
	    conn.setRequestProperty("Access-Control-Allow-Origin", "*");
	    
	    //Send request
		DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
		if (urlParameters.length() > 0)
			wr.writeBytes (urlParameters);
		wr.flush ();
		wr.close ();
		
		//Get Response	
	    InputStream is = conn.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    String line;
	    StringBuffer response = new StringBuffer(); 
	    while((line = rd.readLine()) != null) {
	    	response.append(line);
	    	response.append('\r');
	    }
	    rd.close();
	    
	    return new JSONObject(response.toString());
	}
	
}
