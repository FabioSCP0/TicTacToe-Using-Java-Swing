package com.github.FabioSCP0.TicTacToe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Board implements FieldObserver,PlayerObserver{
	private final int lines = 3;
	private final int columns = 3;
	private List<Field> fields = new ArrayList<Field>();
	private Player playerOne = new Player("x",true); 
	private Player playerTwo = new Player("o",false);
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	public Board(){		
		generateFields();
	}
	
	public void registrarObservador(Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	private void notifyObservers(boolean event) {
		observadores.stream()
			.forEach(ob -> ob.accept(event));
	}
	
	public int getN() {
		return playerOne.getNumOfMoves();
	}
	public int getN2() {
		return playerTwo.getNumOfMoves();
	}
	private void generateFields() {

		playerOne.registerObserver(this);
		playerTwo.registerObserver(this);
		
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				Field field = new Field(line,column,"");
				field.registerObserver(this);
				fields.add(field);
			}
		}
	}
	
	public void toMark(int line, int column) {
		if(this.playerOne.isTurn()) {
			toMark(line, column,playerOne);
			playerOne.setTurn(false);
			playerTwo.setTurn(true);
		}
		else {
			toMark(line, column,playerTwo);
			playerOne.setTurn(true);
			playerTwo.setTurn(false);
		}
	}
	
	private void toMark(int line, int column, Player player) {
		for (Field field : fields) {
			if(field.getLine() == line && field.getColumn() == column && !field.isMarcked()) {
				field.setMarcked(true);
				player.addNumOfMoves();
				field.setCharac(player.getCharac());
			}else if(field.getLine() == line && field.getColumn() == column && field.isMarcked()) {
				field.setMarcked(true);
				field.registerObserver(this);
				player.addNumOfMoves();
			}
		}
	}
	
	public boolean achievedGoal(Player player) {
		boolean goal=false;
		int acumulator;
		int acumulatorD2=0;
		int i=0;
		Field f;
		
		for (int line = 0; line < lines; line++) {
			acumulator=0;
			for (int column = 0; column < columns; column++) {
				f = fields.get(i);
				if(f.getCharac().equalsIgnoreCase(player.getCharac())) 	acumulator++;
				else acumulator=0;
				i++;
			}
			if(acumulator==3) {
				player.setWinner(true);
				
				return true;
			}else goal = false;
		}
		
		for (int column = 0; column < columns; column++) {
			i=column;
			acumulator=0;
			for (int line = 0; line < lines; line++) {
				f = fields.get(i);
				if(f.getCharac().equalsIgnoreCase(player.getCharac())) 	acumulator++;
				
				i+=3;
			}
			if(acumulator==3) {
				player.setWinner(true);
				
				return true;
			}else goal = false;
		}
		
		i=0;
		acumulator=0;
		for (int column = 0; column < columns; column++) {			
			for (int line = 0; line < lines; line++) {
				if(line==column) {
					f = fields.get(i);
					if(f.getCharac().equalsIgnoreCase(player.getCharac())) acumulator++;
				}
				if((line==0&&column==2)||(line==1&&column==1)||(line==2&&column==0)) {
					f = fields.get(i);
					if(f.getCharac().equalsIgnoreCase(player.getCharac())) acumulatorD2++;
				}
				i++;
			}
			if(acumulator==3||acumulatorD2==3) {
				player.setWinner(true);
				
				return true;
			}else goal = false;
			
		}

		return goal;
	}
	
	
	public void restart() {
		fields.stream().forEach(c -> c.restart());
		fields.stream().forEach(c -> c.setCharac("_"));
	}

	@Override
	public void occurredEvent(Field f, FieldEvent e) {
		if(e == FieldEvent.MARCAR) {
			System.out.println("Marcou");	
		}
		if(e==FieldEvent.PERDEUVEZ) {
			System.out.println("Perdeu Vez") ;
		}
	}

	@Override
	public void occurredEvent(Player p, PlayerEvent e) {
		if(e == PlayerEvent.TURN) System.out.println("Turno\n");
		if((achievedGoal(playerOne))&&e == PlayerEvent.WIN) {
			System.out.println("P1 Ganhou");
			System.out.println(e);
			notifyObservers(true);
		}
		else if((achievedGoal(playerTwo))&&e == PlayerEvent.WIN) {
			System.out.println("P2 Ganhou");
			notifyObservers(true);
		}
		if(playerOne.getNumOfMoves()==5&&(!playerOne.isWinner() && !playerTwo.isWinner())) {
			System.out.println("Empate");
			notifyObservers(false);
		}
	}
	

}
