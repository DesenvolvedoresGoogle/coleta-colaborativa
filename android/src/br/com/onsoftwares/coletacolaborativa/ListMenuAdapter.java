package br.com.onsoftwares.coletacolaborativa;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListMenuAdapter extends BaseAdapter {

	private Context context;
	private List<ItemMenu> menuItens;

	public ListMenuAdapter(Context context, ArrayList<ItemMenu> menuItens) {
		this.context = context;
		this.menuItens = menuItens;
		populateMenu();
	}

	@Override
	public int getCount() {
		return this.menuItens.size();
	}

	@Override
	public Object getItem(int position) {
		return this.menuItens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_menu, null);
		}

		ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
		TextView textTitulo = (TextView)convertView.findViewById(R.id.title);
		TextView textCount = (TextView)convertView.findViewById(R.id.counter);

		icon.setImageResource(this.menuItens.get(position).getIcon());
		textTitulo.setText(this.menuItens.get(position).getTitulo());

		if(this.menuItens.get(position).isExibeContador()) {
			textCount.setText(this.menuItens.get(position).getContador());
		} else {
			textCount.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	public void populateMenu() {
		menuItens.add(new ItemMenu("Mudar mapa", R.drawable.menu_ico));
		menuItens.add(new ItemMenu("Pontos próximos", R.drawable.menu_ico));
	}

}