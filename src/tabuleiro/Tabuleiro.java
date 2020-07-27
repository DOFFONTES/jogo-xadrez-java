package tabuleiro;

public class Tabuleiro {
	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 1 || colunas < 1) {
			throw new TabuleiroExcecao("Linha e coluna deve ser maior que 1");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Peca peca(int linhas, int colunas) {
		if(!posicaoExiste(linhas, colunas)) {
			throw new TabuleiroExcecao("Essa posição não existe no tabuleiro!");
		}
		return pecas[linhas][colunas];
	}
	public Peca peca(Posicao posicao) {
		if(!posicaoExiste(posicao)) {
			throw new TabuleiroExcecao("Essa posição não existe no tabuleiro!");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocarPeca(Peca peca, Posicao posicao) {
		if(temPeca(posicao)) {
			throw new TabuleiroExcecao("Já existe peça na posição " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removePeca(Posicao posicao) {
		if(!posicaoExiste(posicao)) {
			throw new TabuleiroExcecao("Essa posição não existe no tabuleiro!");
		}if(peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		
		return aux;
	}
	
	public boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	public boolean posicaoExiste(Posicao posicao) {
		return posicaoExiste(posicao.getLinha(), posicao.getColuna());
	}
	public boolean temPeca(Posicao posicao) {
		if(!posicaoExiste(posicao)) {
			throw new TabuleiroExcecao("Essa posição não existe no tabuleiro!");
		}
		return peca(posicao) != null;
	}
		
}
