package br.com.onsoftwares.coletacolaborativa;

public class ItemMenu {

	private String titulo;
	private int icon;
	private String contador;
	private boolean exibeContador;

	public ItemMenu() {
		this.exibeContador = false;
	}

	public ItemMenu(String titulo, int icon) {
		this.titulo = titulo;
		this.icon = icon;
		this.exibeContador = false;
	}

	public ItemMenu(String titulo, int icon, String contador) {
		this.titulo = titulo;
		this.icon = icon;
		this.contador = contador;
		this.exibeContador = true;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getContador() {
		return contador;
	}

	public void setContador(String contador) {
		this.contador = contador;
	}

	public boolean isExibeContador() {
		return exibeContador;
	}

	public void setExibeContador(boolean exibeContador) {
		this.exibeContador = exibeContador;
	}
}
