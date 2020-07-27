package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override 
	public String toString() {
		return "K";
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();	
	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new  boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		
		//movimento subindo
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//movimento descendo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//movimento para esquerda
		p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//movimento para direita
		p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//movimento noroeste
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//movimento nordeste
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//movimento sudoeste
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//movimento sudeste
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
			
			
		return mat;
	}
}
