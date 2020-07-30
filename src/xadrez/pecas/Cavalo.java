package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Cavalo extends PecaXadrez {

	public Cavalo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "C";
	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new  boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		
		p.setValores(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		if(getTabuleiro().posicaoExiste(p) && oponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}

}
