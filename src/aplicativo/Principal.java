package aplicativo;

import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Principal {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partida = new PartidaXadrez();
		
		while(true) {
			UI.printTabuleiro(partida.getPecas());
			System.out.println("\nOrigem:");
			PosicaoXadrez origem = UI.lerPosicao(sc);
			
			System.out.println("\nDestino:");
			PosicaoXadrez destino = UI.lerPosicao(sc);
			
			PecaXadrez captura = partida.mover(origem, destino);
		}
		
	}

}
