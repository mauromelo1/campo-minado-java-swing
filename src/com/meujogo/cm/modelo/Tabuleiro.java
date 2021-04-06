package com.meujogo.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

	private int linhasTabuleiro;
	private int colunasTabuleiro;
	private int minasTabuleiro;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhasTabuleiro = linhas;
		this.colunasTabuleiro = colunas;
		this.minasTabuleiro = minas;

		gerarCamposTabuleiro();
		associarVizinhosTabuleiro();
		sortearMinasTabuleiro();

	} // fim construtor

	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(obs -> obs.accept(new ResultadoEvento(resultado)));
	}

	public void abrir(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinhaCampo() == linha && c.getColunaCampo() == coluna).findFirst()
				.ifPresent(c -> c.abrirCampo());
	} // fim abrir

	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinhaCampo() == linha && c.getColunaCampo() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacaoCampo());
	} // fim AlternarMarcacao

	private void gerarCamposTabuleiro() {

		for (int l = 0; l < linhasTabuleiro; l++) {
			for (int c = 0; c < colunasTabuleiro; c++) {
				Campo campo = new Campo(l, c);
				campo.registrarObservador(this);
				campos.add(campo);
			} // for c
		} // for l
	} // fim gerarCamposTabuleiro

	private void associarVizinhosTabuleiro() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinhoCampo(c2);
			} // for c2
		} // for c1
	} // fim associarVizinhosTabuleiro

	private void sortearMinasTabuleiro() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinadoCampo();

		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minarCampo();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minasTabuleiro);
	} // sortearMinasTabuleiro

	public boolean objetivoAlcancadoTabuleiro() {
		return campos.stream().allMatch(c -> c.objetivoAlcancadoCampo());
	} // fim objetivoAlcancadoTabuleiro

	public void reiniciarTabuleiro() {
		campos.stream().forEach(c -> c.reiniciarCampo());
		sortearMinasTabuleiro();
	} // fim reiniciarTabuleiro

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			//System.out.println("Perdeu...");
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancadoTabuleiro()) {
			//System.out.println("Ganhou...");
			notificarObservadores(true);
		}
	}

	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinadoCampo()).forEach(c -> c.setAbertoCampo(true));
	}

}
