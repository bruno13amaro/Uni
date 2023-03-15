package Elementos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.Scanner;
import GameEngine.GameEngine;

public class Pontuacao {
	
	private String nickname;
	private int pontos;
	private int turnos;
	
	public Pontuacao(){
		pontos = 0;
		turnos = 0;
	}
	
	public Pontuacao(String nickname, int turnos, int pontos) {
		this.nickname = nickname;
		this.pontos = pontos;
		this.turnos = turnos;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public int getPontos() {
		return pontos;
	}
	
	public int getTurnos() {
		return turnos;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setPontos(int pontos) {
		this.pontos += pontos;
	}
	
	public void setTurnos() {
		turnos ++;
	}
	
	public void resetPontos() {
		pontos = 0;
	}
	
	public void resetTurnos() {
		turnos = 0;
	}
	
	public void lerInserir(File f) {
		PriorityQueue<Pontuacao> q = new PriorityQueue<>((p1, p2) -> p2.getPontos() - p1.getPontos());
		try {
			Scanner s = new Scanner(f);
			while(s.hasNextLine()) {
				String[] aux = s.nextLine().split(":");
				q.offer(new Pontuacao(aux[0], Integer.parseInt(aux[1]), Integer.parseInt(aux[2])));
			}
			s.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("Problema ao salvar a pontuação");
			return;
		}
		q.offer(this);
		String nome = f.getName();
		f.delete();
		File f2 = new File(GameEngine.LEVEL_PONT + "/" + nome);
		inserir(q, f2);
	}
	
	private void inserir(PriorityQueue<Pontuacao> q, File f) {
		try {
			PrintWriter w = new PrintWriter(f);
			for(int i = 0; i < 5; i ++) {
				if(q.size() == 0) break;
				Pontuacao p = q.poll();
				w.println(p.getNickname() + ":" + p.getTurnos() + ":" + p.getPontos());
			}
			w.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("Problema ao salvar a pontuação");
		}
	}
	
	public void inserir(File f) {
		try {
			PrintWriter w = new PrintWriter(f);
			w.print(nickname + ":" + turnos + ":" + pontos);
			w.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("Problema em salvar a pontuação");
		}
	}

}
