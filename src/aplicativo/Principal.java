package aplicativo;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezExcecao;

public class Principal {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partida = new PartidaXadrez();
		
		while(true) {
			
			try {
			UI.limparTela();
			UI.printTabuleiro(partida.getPecas());
			System.out.println("\nOrigem:");
			PosicaoXadrez origem = UI.lerPosicao(sc);
			
			System.out.println("\nDestino:");
			PosicaoXadrez destino = UI.lerPosicao(sc);
			
			PecaXadrez captura = partida.mover(origem, destino);
			
			}catch(XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}
