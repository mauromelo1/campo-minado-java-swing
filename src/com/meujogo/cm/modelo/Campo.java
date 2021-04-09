package com.meujogo.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int linhaCampo;
	private final int colunaCampo;

	// Caracteristicas do Campo
	private boolean abertoCampo = false;
	private boolean minadoCampo = false;
	private boolean marcadoCampo = false;

	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();

	public Campo(int linha, int coluna) {
		this.linhaCampo = linha;
		this.colunaCampo = coluna;
	} // fim construtor

	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach( obs -> obs.eventoOcorreu(this, evento));
	}

	boolean adicionarVizinhoCampo(Campo candidatoVizinho) {
		boolean linhaDiferente = (linhaCampo != candidatoVizinho.linhaCampo);
		boolean colunaDiferente = (colunaCampo != candidatoVizinho.colunaCampo);
		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(linhaCampo - candidatoVizinho.linhaCampo);
		int deltaColuna = Math.abs(colunaCampo - candidatoVizinho.colunaCampo);
		int deltaGeral = deltaLinha + deltaColuna;

		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else {
			return false;
		}
	} // fim adicionarVizinhoCampo

	public void alternarMarcacaoCampo() {
		if (!abertoCampo) {
			marcadoCampo = !marcadoCampo;
			
			if (marcadoCampo) {
				notificarObservadores(CampoEvento.MARCAR);
			}
			else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	} // fim alternarMarcacaoCampo

	public boolean abrirCampo() {
		if (!abertoCampo && !marcadoCampo) {
			if (minadoCampo) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			} // fim if do campoMinado
			
			setAbertoCampo(true);			
			
			if (vizinhancaSeguraCampo()) {
				vizinhos.forEach(v -> v.abrirCampo());
			} // fim if vizinhancaSegura

			return true;

		} // fim if !abertoCampo && !marcadoCampo
		else {
			return false;
		}
	} // fim abrirCampo

	public boolean vizinhancaSeguraCampo() {
		return vizinhos.stream().noneMatch(v -> v.minadoCampo);
	} // fim vizinhancaSeguraCampo

	void minarCampo() {
		minadoCampo = true;
	} // fim minarCampo

	public boolean isMinadoCampo() {
		return minadoCampo;
	} // fim isMinadoCampo

	public boolean isMarcadoCampo() {
		return marcadoCampo;
	} // fim isMarcadoCampo

	void setAbertoCampo(boolean aberto) {
		this.abertoCampo = aberto;
		
		if (aberto) {			
			notificarObservadores(CampoEvento.ABRIR);			
		}
	} // setAbertoCampo

	public boolean isAbertoCampo() {
		return abertoCampo;
	} // fim isAbertoCampo

	public boolean isFechadoCampo() {
		return !isAbertoCampo();
	} // fim isFechadoCampo

	public int getLinhaCampo() {
		return linhaCampo;
	} // fim getLinhaCampo

	public int getColunaCampo() {
		return colunaCampo;
	} // fim getColunaCampo

	boolean objetivoAlcancadoCampo() {
		boolean desvendado = !minadoCampo && abertoCampo;
		boolean protegido = minadoCampo && marcadoCampo;

		return desvendado || protegido;
	} // fim objetivoAlcancadoCampo

	public int minasNaVizinhancaCampo() {
		return (int)vizinhos.stream().filter(v -> v.minadoCampo).count();
	} // fim minasNavizinhancaCampo

	void reiniciarCampo() {
		abertoCampo = false;
		minadoCampo = false;
		marcadoCampo = false;
	} // fim reiniciarCampo

} // fim da Class Campo
