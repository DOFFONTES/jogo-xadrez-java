package aplicativo;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezExcecao;

public class Principal {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partida = new PartidaXadrez();
		List<PecaXadrez> capturada = new ArrayList<>();
		
		while(!partida.getCheckMate()) {
			
			try {
				UI.limparTela();
				UI.printPartida(partida, capturada);
				System.out.print("\nOrigem: ");
				PosicaoXadrez origem = UI.lerPosicao(sc);
				
				boolean[][] possiveisDestinos = partida.possiveisMovimentos(origem);
				UI.limparTela();
				UI.printTabuleiro(partida.getPecas(), possiveisDestinos);
				
				System.out.print("\nDestino: ");
				PosicaoXadrez destino = UI.lerPosicao(sc);
				
				PecaXadrez captura = partida.mover(origem, destino);
				
				if(captura != null) {
					capturada.add(captura);
				}
				if(partida.getPromocao() != null) {
					System.out.print("Peca que sera promovida(C/T/B/R): ");
					String tipo = sc.nextLine().toUpperCase();
					while(!tipo.equals("C") && !tipo.equals("T") && !tipo.equals("R") && !tipo.equals("B")) {
						System.out.print("Opcao invalida, favor digitar novamente. Peca que sera promovida(C/T/B/R): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partida.substituirPeca(tipo);
				}
				
			}catch(XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.limparTela();
		UI.printPartida(partida, capturada);		
		
	}

}
