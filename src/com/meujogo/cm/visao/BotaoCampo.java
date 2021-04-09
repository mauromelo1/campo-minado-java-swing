package com.meujogo.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.meujogo.cm.modelo.Campo;
import com.meujogo.cm.modelo.CampoEvento;
import com.meujogo.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	private final Color BG_PADRAO = new Color(184, 184, 184);
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	private Campo campo;

	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));

		addMouseListener(this);
		campo.registrarObservador(this);
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;

		case MARCAR:
			aplicarEstiloMarcar();
			break;

		case EXPLODIR:
			aplicarEstiloExplodir();
			break;

		default:
			aplicarEstiloPadrao();
		}

	}

	private void aplicarEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(campo.isMinadoCampo()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		setBackground(BG_PADRAO);		

		switch (campo.minasNaVizinhancaCampo()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;

		case 2:
			setForeground(Color.BLUE);
			break;

		case 3:
			setForeground(Color.YELLOW);
			break;

		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}
		
		String valor  = !campo.vizinhancaSeguraCampo() ? 
				campo.minasNaVizinhancaCampo() + "" : "";
		setText(valor);

	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setText("");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrirCampo();
			System.out.println("Bot�o esquerdo!");
		} else {
			campo.alternarMarcacaoCampo();
			System.out.println("Outro bot�o!");
		}

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
