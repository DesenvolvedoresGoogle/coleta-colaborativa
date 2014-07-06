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
import android.widget.TextView;

public class ListTiposAdapter extends BaseAdapter {

	private Context context;
	private List<ItemTipo> tipoItens;

	public ListTiposAdapter(Context context, ArrayList<ItemTipo> tipoItens) {
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
			convertView = inflater.inflate(R.layout.list_item_tipo, null);
		}
		
		//TODO: Resolver bug do espaçamento no texto
		
		TextView textView = (TextView) convertView.findViewById(R.id.list_tipo_textview);
		textView.setText(tipoItens.get(position).getNome());

		return convertView;
	}
	

}