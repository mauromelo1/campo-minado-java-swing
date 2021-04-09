package com.meujogo.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.meujogo.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(tabuleiro.getLinhasTabuleiro(), tabuleiro.getColunasTabuleiro()));

		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));

		SwingUtilities.invokeLater(() -> {
			tabuleiro.registrarObservador(e -> {
				if (e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				tabuleiro.reiniciarTabuleiro();
			});
		});
	}

}
