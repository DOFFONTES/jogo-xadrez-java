package xadrez;

import java.util.ArrayList;
import java.util.List;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	
	private List<Peca> pecasTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaXadrez() {
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		tabuleiro = new Tabuleiro(8, 8);
		inicio();
	}
	
	public int getTurno() {
		return turno;
	}
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];

		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoOrigem){
		Posicao posicao = posicaoOrigem.conversao();
		validarPosicaoOrigem(posicao);
		
		return tabuleiro.peca(posicao).possiveisMovimentos();
	}
	
	public PecaXadrez mover(PosicaoXadrez origem, PosicaoXadrez destino) {
		Posicao ori = origem.conversao();
		Posicao desti = destino.conversao();
		validarPosicaoOrigem(ori);
		validarPosicaoDestino(ori, desti);
		Peca pecaCapturada = movendo(ori, desti);
		proximoTurno();
		
		return (PecaXadrez)pecaCapturada;		
	}
	
	private Peca movendo(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecasTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.temPeca(posicao)) {
			throw new XadrezExcecao("Não existe peça nessa posição");
		}
		if(jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("Peca escolhida nao e sua");
		}
		if(!tabuleiro.peca(posicao).UmMovimento()) {
			throw new XadrezExcecao("Nao tem movimento possivel");
		}
	}
	
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).possivelMovimento(destino)) {
			throw new XadrezExcecao("A peca escolhida nao pode movintar para o destino escolhido");
		}
	}
	
	public void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).conversao());
		pecasTabuleiro.add(peca);
	}

	private void inicio() {
		//novaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));

		novaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

		novaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
	}

}
