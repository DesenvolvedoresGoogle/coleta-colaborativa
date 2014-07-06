package br.com.onsoftwares.coletacolaborativa;

import java.util.ArrayList;

public class ColetaMarker {
	private String pontoId;
	private ArrayList<ItemTipo> tipos = new ArrayList<ItemTipo>();
	private String descricao;
	private boolean privado;
	private String email;
	private String nomeUsuario;
	private double latitude;
	private double longitude;
	
	
	public ColetaMarker(ArrayList<ItemTipo> tipos, String descricao, boolean privado,
			String email, String nomeUsuario) {
		super();
		this.tipos = tipos;
		this.descricao = descricao;
		this.privado = privado;
		this.email = email;
		this.nomeUsuario = nomeUsuario;
	}

	public ColetaMarker(String pontoId, ArrayList<ItemTipo> tipos,
			String descricao, boolean privado, String email,
			String nomeUsuario, double latitude, double longitude) {
		super();
		this.pontoId = pontoId;
		this.tipos = tipos;
		this.descricao = descricao;
		this.privado = privado;
		this.email = email;
		this.nomeUsuario = nomeUsuario;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	public String getPontoId() {
		return pontoId;
	}

	public void setPontoId(String pontoId) {
		this.pontoId = pontoId;
	}

	public ArrayList<ItemTipo> getTipos() {
		return tipos;
	}
	public void setTipos(ArrayList<ItemTipo> tipos) {
		this.tipos = tipos;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean isPrivado() {
		return privado;
	}
	public void setPrivado(boolean privado) {
		this.privado = privado;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
