package br.com.onsoftwares.coletacolaborativa;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListTiposCheckAdapter extends BaseAdapter {

	private Context context;
	private List<ItemTipo> tipoItens;
	
	public ListTiposCheckAdapter(Context context, ArrayList<ItemTipo> tipoItens) {
		this.context = context;
		this.tipoItens = tipoItens;
	}

	@Override
	public int getCount() {
		return this.tipoItens.size();
	}

	@Override
	public Object getItem(int position) {
		return this.tipoItens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_tipo_check, null);
		}
		
		
		TextView textView = (TextView) convertView.findViewById(R.id.list_tipo_check_textview);
		textView.setText(tipoItens.get(position).getNome());
		
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.list_tipo_check_checkbox);
		if (tipoItens.get(position).isChecked()) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		
		return convertView;
	}
	
	public String getListaTipos() {
		String response = "";
		
		for (ItemTipo t : tipoItens)
			if (t.isChecked()) 
				response += t.getId() + ",";
		
		if(response.length() > 0)
			response = response.substring(0, response.length()-1);
			
		return response;
	}
	

}