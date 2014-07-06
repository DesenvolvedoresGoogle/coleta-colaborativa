package br.com.onsoftwares.coletacolaborativa;

public class ItemTipo {
	private String nome;
	private String id;
	private boolean checked;
	
	public ItemTipo(String id, String nome) {
		super();
		this.nome = nome;
		this.id = id;
		this.checked = false;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return this.nome;
	}
}
