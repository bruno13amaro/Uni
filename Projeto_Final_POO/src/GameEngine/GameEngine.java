package GameEngine;


import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import Elementos.*;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.observer.Observed;
import pt.iul.ista.poo.observer.Observer;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class GameEngine implements Observer{
	
	private final static File LEVEL_DIR = new File("levels");
	public final static File LEVEL_PONT = new File("pontuacoes");
	
	public static final int  GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	
	private static GameEngine INSTANCE;
	
	private ImageMatrixGUI gui;
	
	private boolean inicio = true; 
	
	private ArrayList<ImageTile> elementos;
	private Fireman f;
	private Pontuacao pontuacao;
	
	private File[] mapas;
	private int mapa;
	
	
	private GameEngine(){
		gui = ImageMatrixGUI.getInstance();
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);
		gui.registerObserver(this);
		gui.go();
		elementos = new ArrayList<>();
		pontuacao = new Pontuacao();
		mapas = LEVEL_DIR.listFiles();
		mapa = 0;
	}
	
	public static GameEngine getInstance() {
		if(INSTANCE == null)
			INSTANCE = new GameEngine();
		return INSTANCE;
	}
	
	public void start() {
		if(pontuacao.getNickname() == null)
			getNickName();
		createElements(new File(LEVEL_DIR + "/" + mapas[mapa].getName()));
		sendImages();
	}
	
	public void getNickName() {
		System.out.println("Introduza o seu NickName: ");
		Scanner s = new Scanner(System.in);
		if(s.hasNextLine())
			pontuacao.setNickname(s.nextLine());
		s.close();
	}
	
	public Fireman getFireman() {
		return f;
	}
	
	public Terreno getTerreno(Point2D p) {
		for(ImageTile i : elementos)
			if(i.getPosition().equals(p) && i.getLayer() == 0)
				return (Terreno) i;
		return null;
	}
	
	public Fire getFire(Point2D p) {
		for(ImageTile i: elementos)
			if(i.getPosition().equals(p) && i.getLayer() == 1)
				return (Fire) i;
		return null;
	}
	
	public ArrayList<Fire> getFires(){
		ArrayList<Fire> fires = new ArrayList<>();
		for(ImageTile i : elementos)
			if(i.getLayer() == 1)
				fires.add((Fire) i);
		return fires;
	}
	
	public ArrayList<Truck> getTrucks(){
		ArrayList<Truck> bull = new ArrayList<>();
		for(ImageTile i : elementos)
			if(i.getLayer() == 3)
				bull.add((Truck) i);
		return bull;
	}
	
	public ArrayList<Updatable> getUpdatables(){
		ArrayList<Updatable> up = new ArrayList<>();
		for(ImageTile i : elementos) {
			GameElement aux = (GameElement) i;
			if(aux.isUpdatable())
				up.add((Updatable)aux);
		}
		return up;
	}
	
	public void createElements(File f) {
		try{
			Scanner s = new Scanner(f);
			int y = 0;
			while(s.hasNextLine() && y < GRID_HEIGHT) {
				String line = s.nextLine();
				for(int x = 0; x < GRID_WIDTH; x ++)
					elementos.add(GameElement.criar(String.valueOf(line.charAt(x)), x, y));
				y++;
			}
			while(s.hasNextLine()) {
				String[] a = s.nextLine().split(" ");
				if(a[0].equals("Fireman")) {
					this.f = new Fireman(Integer.parseInt(a[1]), Integer.parseInt(a[2]));
					elementos.add(this.f);
				}else {
				    if(a[0].equals("Fire")) {
					   Point2D p = new Point2D(Integer.parseInt(a[1]), Integer.parseInt(a[2]));
					   createFire(p);
				    }else
				    	elementos.add(GameElement.criar(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2])));
				    }
				}
			inicio = false;
			s.close();
			gui.update();
			}catch(FileNotFoundException e) {
				System.err.println("Problema a ler o ficheiro do mapa");
				mapa ++;
				start();
			}
		
	}
	
	public void addElement(ImageTile i) {
		elementos.add(i);
		gui.addImage(i);
	}
	
	public void removeElement(ImageTile i) {
		elementos.remove(i);
		gui.removeImage(i);
	}
	
	
	
	public void createPlane() {
		int[] aux = new int[GRID_WIDTH];
		ArrayList<Fire> fires = getFires();
		for(Fire f : fires)
			aux[f.getPosition().getX()] ++;
		Plane p = new Plane(getPlaneColuna(aux), GRID_HEIGHT - 1);
		elementos.add(p);
		gui.addImage(p);
		pontuacao.setPontos(-25);
	}
	
	public void createFire(Point2D position) {
		BurnableTerrain aux = (BurnableTerrain)getTerreno(position);
		aux.putBurning();
		Fire f = new Fire(position.getX(), position.getY());
		if(inicio) 
			elementos.add(f);
		else {
			gui.addImage(f);
			elementos.add(f);
			pontuacao.setPontos(-20);
		}
	}
	
	public void deleteFire(BurnableTerrain b) {
		b.resetTurnos();
		Fire aux = getFire(b.getPosition());
		gui.removeImage(aux);
		elementos.remove(aux);
	}

	@Override
	public void update(Observed source) {
		boolean passarTurno = true;
		while(passarTurno == true) {
			int key = gui.keyPressed();
			if(Direction.isDirection(key)) {
				f.move(key);
				passarTurno = false;
			}
			if(key == KeyEvent.VK_ENTER && f.getTruck() != null) {
				f.getTruck().retirarFireman();
				passarTurno = false;
			}
			if(key == KeyEvent.VK_P) {
				createPlane();
				passarTurno = false;
			}
		}
		pontuacao.setTurnos();
		updateMap();
		gui.setStatusMessage("NickName: " + pontuacao.getNickname() + "      Jogadas: " + pontuacao.getTurnos() + "        Pontos: " + pontuacao.getPontos());
		gui.update();
	}
	
	public int getPlaneColuna(int[] aux) {
		int max = 0;
		for(int i = 1; i < aux.length; i ++)
			if(aux[max] < aux[i])
				max = i;
		return max;
	}
	
	public void changePontos(int pontos) {
		pontuacao.setPontos(pontos);
	}
	
	public void updateMap() {
		getUpdatables().forEach(p -> p.update());
		ArrayList<Fire> fires = getFires();
		if(fires.size() == 0) {
			endGame();
			return;
		}
		//getUpdatables().forEach(p -> p.update());
	}
	
	public void endGame() {
		savePontuacao();
		mapa ++;
		if(mapa == mapas.length)
			mapa = 0;
		inicio = true;
		clear();
		start();
	}
	
	public void savePontuacao() {
		File f = new File(LEVEL_PONT + "/" + mapas[mapa].getName());
		if(f.exists())
			pontuacao.lerInserir(f);
		else
			pontuacao.inserir(f);
	}
	
	public void clear() {
		f = null;
		pontuacao.resetPontos();
		pontuacao.resetTurnos();
		gui.removeImages(elementos);
		elementos.clear();
	}
	
	public boolean isPoint(Point2D p) {
		if(p.getX() < 0 || p.getY() < 0) return false;
		if(p.getX() >= GRID_WIDTH || p.getY() >= GRID_HEIGHT) return false;
		return true;
	}
	
	
	public boolean haveMore(Point2D p) {
		for(ImageTile i : elementos)
			if(i.getPosition().equals(p) && i.getLayer() != 0)
				return true;
		return false;
	}
	
	
	public boolean isOnFire(Point2D p) {
		Terreno aux = getTerreno(p);
		if(aux.isBurning())
			return true;
		return false;
	}
	
	public void sendImages() {
		gui.addImages(elementos);
	}
}