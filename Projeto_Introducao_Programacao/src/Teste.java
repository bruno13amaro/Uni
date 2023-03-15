class Teste {
	
	//Teste de funções da classe Images
	
	static ColorImage testecopy() {
		ColorImage img = new ColorImage("objc1.png");
		ColorImage img1 = new ColorImage("sporting.png");
		ColorImage result = Images.copy(img,img1);
		return result;
	}
	
	static ColorImage testefundo() {
		ColorImage teste = new ColorImage("sporting.png");
		ColorImage result = Images.fundo(teste, 1000, 1000);
		return result;
	}
	
	static ColorImage testeCircle() {
		ColorImage teste = new ColorImage("objc1.png");
		ColorImage result = Images.circle(80,100,50,teste);
		return result;
	}
	
	static ColorImage testescale() {
		ColorImage teste = new ColorImage("sporting.png");
		ColorImage resultP = Images.scale(teste, .75);
		ColorImage resultG = Images.scale(resultP, 2.5);
		return resultG;
	}
	
	static ColorImage testecopyCinza() {
		ColorImage teste = new ColorImage("sporting.png");
		ColorImage result = Images.copyCinza(teste);
		return result;
	}
	
	//Teste de métodos da Classe Layer
	
	static ColorImage testeLayer() {
		ColorImage a = new ColorImage("objc1.png");
		Layer teste = new Layer(a);
		teste.changeFactor(.75);
		teste.setX(100);
		teste.setY(100);
		teste.changeName("Velhinho");
		teste.deactivateImage();
		teste.activateImage(a);
		ColorImage f = teste.doLayer(500,500);
		return f;
	}
	
	
	//Teste de métodos da Classe Poster
	
	static ColorImage testePoster() {
		ColorImage a = new ColorImage("objc1.png");
		ColorImage b = new ColorImage("melancia.png");
		ColorImage c = new ColorImage("franca.png");
		ColorImage d = new ColorImage("sporting.png");
		Layer um = new Layer(a);
		Layer dois = new Layer(c);
		Layer tres = new Layer(d);
		
		Poster teste = new Poster(1000,1000);
		
		teste.layer0(b);
		teste.addLayerFinal(dois);
		teste.add(2, um);
		teste.addLayerFinal(tres);
		teste.remove(2);
		teste.swapLayers(1, 2);
		ColorImage f = teste.doPoster();
		return f;
		}
	
	

}