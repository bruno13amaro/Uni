class Layer {
	
	//Atributos
	ColorImage img;
	double factor;
	int x;
	int y;
	String s;
	
	//Criação de Objetos Layer
	
	//1
	Layer (ColorImage img, double factor, int x, int y, String s){
		this.img = img;
		this.factor = factor;
		this.x = x;
		this.y = y;
		this.s = s;
		
	}
	
	//2
	Layer(ColorImage img, int x, int y){
		this.img = img;
		factor = 1;
		this.x = x;
		this.y = y;
		s = "";
	}
	
	//3
	Layer(ColorImage img){
		this.img = img;
		factor = 1;
		x = 0;
		y = 0;
		s = "";
	}
	
	//Métodos
	
	//Modificar Nome da Imagem
	void changeName(String s) {
		this.s = s;
	}
	
	//Modificar Escala e Posicionamento da Imagem
	void changeFactor(double factor) {
		this.factor = factor;
	}
	
	void setX(int x) {
		this.x = x;
	}
	
	void setY(int y) {
		this.y = y;
	}
	
	//Definir Imagem com Ativa/Inativa
	void activateImage(ColorImage img) {
		this.img = img;
	}
	
	void deactivateImage() {
		this.img = null;
	}
	
	//Devolver Camada Inteira
	ColorImage doLayer(int width, int height) {
		ColorImage layer = new ColorImage(width, height);
		for (int i = 0; i < layer.getWidth(); i ++)
			for (int j = 0; j < layer.getHeight(); j ++)
				layer.setColor(i, j, Images.TRANSPARENTE);
		if(this.img == null)
			return layer;
		else {
			ColorImage result = Images.scale(img, factor);
			if (x+result.getWidth() > width || y+result.getHeight() > height)
				throw new IllegalArgumentException("Imagem não é totalmente utilizada");
			int a = x;
			int b = y;
			for (int i = 0; i < result.getWidth(); i ++) {
				for (int j = 0; j < result.getHeight(); j ++) {
					Color aux = result.getColor(i, j);
					layer.setColor(a, b, aux);
					b ++;
				}
				a ++;
				b = y;
		    }
			return layer;
		}
	}
	
}