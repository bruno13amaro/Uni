class Poster {
	
	//Atributos
	Layer[] l;
	ColorImage tela;
	int acesso;
	final int size = 10;
	
	//Criação de Objetos tipo Poster
	Poster(int width, int height){
		tela = new ColorImage(width, height);
		for (int i = 0; i < tela.getWidth(); i ++)
			for (int j = 0; j < tela.getHeight(); j ++)
				tela.setColor(i, j, Images.TRANSPARENTE);
		acesso = 0;
		l = new Layer[size];
	}
	
	//Métodos
	//Criar/Substituir Layer[0]
	void layer0(ColorImage img) {
		Layer a = new Layer(Images.fundo(img, tela.getWidth(), tela.getHeight()));
		if (acesso == 0) {
			l[0] = a;
			acesso ++;
		}else {
			l[0] = a;
		}
	}
	
	//Adicionar nova Layer no fim da Coleção
	void addLayerFinal(Layer a) {
		if (acesso == l.length)
			throw new IllegalStateException ("Sem espaço para mais layers!");
		else {
			l[acesso] = a;
			acesso ++;
		}
	}
	
	//Remover uma Layer
	void remove(int n) {
		if (l[n] == null)
			throw new NullPointerException ("Não se retira o vazio!");
		for (int i = n; i < acesso; i ++)
			l[i] = l[i+1];
		acesso --;
	}
	
	//Adicionar uma Layer
	void add(int n, Layer a) {
		if (acesso == l.length)
			throw new IllegalStateException ("Sem espaço para mais layers");
		if (n > acesso)
			throw new IllegalArgumentException ("Posição inválida");
		if (n == acesso) {
			addLayerFinal(a);
			return;
		}
		for (int i = acesso-1; i >= n; i --) {
			l[i+1] = l[i];
		}
		l[n] = a;
		acesso ++;
	}
	
	//Trocar Posições de Layers
	void swapLayers(int n, int m) {
		Layer swap = l[n];
		l[n] = l[m];
		l[m] = swap;
	}
	
	//Obter Poster Final
	ColorImage doPoster() {
		if (acesso == 0)
			return tela;
		for (int i = 0; i < acesso; i ++) {
			Images.copy(tela, l[i].doLayer(tela.getWidth(), tela.getHeight()));
		}
		return tela;
	}
}