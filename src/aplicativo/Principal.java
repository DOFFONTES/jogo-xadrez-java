package aplicativo;

import xadrez.PartidaXadrez;

public class Principal {

	public static void main(String[] args) {
		
		PartidaXadrez partida = new PartidaXadrez();
		UI.printTabuleiro(partida.getPecas());
	}

}
