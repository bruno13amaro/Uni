class Images {
	
	//Transparência
	static final Color TRANSPARENTE = new Color(255,255,255);
	
	//Transposição de imagens
	static ColorImage copy(ColorImage base, ColorImage b) {
		int w = Math.min(base.getWidth(), b.getWidth());
		int h = Math.min(base.getHeight(), b.getHeight());
		for (int i = 0; i < w; i ++)
			for (int j = 0; j < h; j ++) {
				Color aux = b.getColor(i, j);
				if(!aux.isEqual(TRANSPARENTE))
					base.setColor(i,j,aux);
			}
		return base;
	}
	
	
	//Criar fundo de um poster
	static ColorImage fundo(ColorImage img, int width, int height) {
		ColorImage result = new ColorImage(width, height);
		int a = 0;
		int b = 0;
		for (int i = 0; i < result.getWidth(); i ++) {
			for (int j = 0; j < result.getHeight(); j ++) {
				Color aux = img.getColor(a, b);
				result.setColor(i, j, aux);
				b ++;
				if (b == img.getHeight())
					b = 0;
			}
			b = 0;
			a ++;
			if (a == img.getWidth())
				a = 0;
		}
		return result;
	}
	
	
	//Criar uma Imagem Baseada numa circunferencia
	static ColorImage circle(int x, int y, int r, ColorImage img) {
		if (2*r>img.getWidth() || 2*r>img.getHeight())
			throw new IllegalArgumentException("Valores sem sentido!");
		if (r > x || r > y)
			throw new IllegalArgumentException("Valores sem sentido");
		int a = Math.abs(x - r);
		int b = Math.abs(y - r);
		ColorImage result = new ColorImage(2*r, 2*r);
		for (int i = 0; i < result.getWidth(); i ++) {
			for (int j = 0; j < result.getHeight(); j ++) {
				if ((int)(Math.pow(x-a, 2)+Math.pow(y-b, 2))<(int)(Math.pow(r,2)))
					result.setColor(i, j, img.getColor(a, b));
				else
					result.setColor(i, j, TRANSPARENTE);
				b ++;
			}
			a ++;
			b = y - r;
		}
		return result;
	}
	
	//Escalar Imagem
	static ColorImage scale(ColorImage img, double factor) {
		ColorImage result = new ColorImage((int)(img.getWidth()*factor), (int)(img.getHeight()*factor));
		if (factor < 1)
			for (double i = 0; i < result.getWidth(); i += factor)
				for (double j = 0; j < result.getHeight(); j += factor) {
					Color aux = img.getColor((int)(i/factor), (int)(j/factor));
					result.setColor((int)i, (int)j, aux);
				}
		if (factor >= 1)
			for (double i = 0; i < result.getWidth(); i ++)
				for (double j = 0; j < result.getHeight(); j ++) {
					Color aux = img.getColor((int)(i/factor), (int)(j/factor));
					result.setColor((int)i, (int)j, aux);
				}
		return result;
	}
	
	
	//Copiar imagem em tons de cinza
	static ColorImage copyCinza(ColorImage img) {
		ColorImage result = new ColorImage(img.getWidth(), img.getHeight());
		for (int i = 0; i < result.getWidth(); i ++)
			for (int j = 0; j < result.getHeight(); j ++) {
				Color aux = img.getColor(i, j);
				int cinza = (int)(0.3*aux.getR() + 0.59*aux.getG() + 0.11*aux.getB());
				Color cinz = new Color(cinza,cinza,cinza);
				result.setColor(i, j, cinz);
			}
		return result;
	}
	
}