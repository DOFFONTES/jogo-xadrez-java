package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassant;
	private PecaXadrez promocao;
	
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
	public boolean getCheck() {
		return check;
	}
	public boolean getCheckMate() {
		return checkMate;
	}
	public PecaXadrez getEnPassant() {
		return enPassant;
	}
	public PecaXadrez getPromocao() {
		return promocao;
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
		
		if(testeCheck(jogadorAtual)) {
			desfazerMovimento(ori, desti, pecaCapturada);
			throw new XadrezExcecao("Voce nao pode se colocar em check");
		}
		
		PecaXadrez pecaMovimentada = (PecaXadrez) tabuleiro.peca(desti);
		
		promocao = null;
		if(pecaMovimentada instanceof Peao) {
			if((pecaMovimentada.getCor() == Cor.BRANCO && desti.getLinha() == 0) || (pecaMovimentada.getCor() == Cor.PRETO && desti.getLinha() == 7)) {
				promocao = (PecaXadrez) tabuleiro.peca(desti);
			}
		}
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}else {
			proximoTurno();
		}
		//movimento especial en passant
		if (pecaMovimentada instanceof Peao && (desti.getLinha() == ori.getLinha() + 2 || desti.getLinha() == ori.getLinha() - 2)) {
			enPassant = pecaMovimentada;
		}else {
			enPassant = null;
		}
			
		return (PecaXadrez)pecaCapturada;		
	}
	
	public PecaXadrez substituirPeca(String tipo) {
		if(promocao == null) {
			throw new IllegalStateException("Peca nao pode ser promovida");
		}
		if (!tipo.equals("C") && !tipo.equals("T") && !tipo.equals("R") && !tipo.equals("B")) {
			throw new InvalidParameterException("Tipo de promocao invalida");
		}
		Posicao pos = promocao.getPosicaoXadrez().conversao();
		Peca p = tabuleiro.removePeca(pos);
		pecasTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(tipo, promocao.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasTabuleiro.add(novaPeca);
				
		return novaPeca;
	}
	
	private PecaXadrez novaPeca(String tipo, Cor cor) {
		if(tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if(tipo.equals("T")) return new Torre(tabuleiro, cor);
		if(tipo.equals("R")) return new Rainha(tabuleiro, cor);
		return new Bispo(tabuleiro, cor);
	}
	
	private Peca movendo(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(origem);
		p.incrementarMovimento();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecasTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		// movimento especial Roque pequeno(do lado do rei)
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre =(PecaXadrez) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.incrementarMovimento();
		}
		// movimento especial Roque grande(do lado da rainha)
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre =(PecaXadrez) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.incrementarMovimento();
		}
		
		//movimento especial en passant
		if (p instanceof Peao && origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
			Posicao posicaoPeao;
			if(p.getCor() == Cor.BRANCO) {
				posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
			}else {
				posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
			}
			pecaCapturada = tabuleiro.removePeca(posicaoPeao);
			pecasTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturada) {
		PecaXadrez p =(PecaXadrez) tabuleiro.removePeca(destino);
		p.decrementarMovimento();
		tabuleiro.colocarPeca(p, origem);
		
		if(capturada != null) {
			tabuleiro.colocarPeca(capturada, destino);
			pecasCapturadas.remove(capturada);
			pecasTabuleiro.add(capturada);
		}
		
		// movimento especial Roque pequeno(do lado do rei)
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre =(PecaXadrez) tabuleiro.removePeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.decrementarMovimento();
		}
		// movimento especial Roque grande(do lado da rainha)
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre =(PecaXadrez) tabuleiro.removePeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.decrementarMovimento();
		}
		//movimento especial en passant
		if (p instanceof Peao && origem.getColuna() != destino.getColuna() && capturada == enPassant) {
			PecaXadrez peao = (PecaXadrez) tabuleiro.removePeca(destino);
			Posicao posicaoPeao;
			if(p.getCor() == Cor.BRANCO) {
				posicaoPeao = new Posicao(3, destino.getColuna());
			}else {
				posicaoPeao = new Posicao(4, destino.getColuna());
			}
			tabuleiro.colocarPeca(peao, posicaoPeao);			
		}
		
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
	
	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
	
		for(Peca p : lista) {
			if(p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Nao existe rei " + cor + " no tabuleiro");
	}
	
	private boolean testeCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().conversao();
		List<Peca> pecaOponente = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for(Peca p : pecaOponente) {
			boolean[][] mat = p.possiveisMovimentos();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;	
	}
	
	private boolean testeCheckMate(Cor cor) {
		if(!testeCheck(cor)) {
			return false;
		}
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : lista) {
			boolean[][] mat = p.possiveisMovimentos();
			for(int i = 0; i < tabuleiro.getLinhas(); i++) {
				for(int j = 0; j < tabuleiro.getColunas(); j++) {
					if(mat[i][j]) {
					    Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().conversao();
						Posicao destino = new Posicao(i, j);
						Peca captura = movendo(origem, destino);
						boolean teste = testeCheck(cor);
						desfazerMovimento(origem, destino, captura);
						if(!teste) {
							return false;
						}
					}
				}
			}
		}			
		return true;
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).conversao());
		pecasTabuleiro.add(peca);
	}

	private void inicio() {

		novaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		novaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		novaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		novaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		novaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		novaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		novaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		novaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

		novaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		novaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		novaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		novaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		novaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		novaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		novaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));		
	}

}
