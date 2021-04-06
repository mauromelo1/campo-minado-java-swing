package com.meujogo.cm.visao;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.meujogo.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(tabuleiro.getLinhasTabuleiro(), tabuleiro.getColunasTabuleiro()));

		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		tabuleiro.registrarObservador( e -> {
			// TODO mostrar resultado para o usuário!
		});

	}

}
