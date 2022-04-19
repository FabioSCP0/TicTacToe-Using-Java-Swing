package com.github.FabioSCP0.TicTacToe.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private int numOfMoves=0;
	private boolean winner=false;
	private String charac;
	private boolean turn = false;
	private List<PlayerObserver> observers = new ArrayList<>();
	
	public Player(String charac, boolean turn){
		this.charac = charac;
		this.turn = turn;
	}

	public void registerObserver(PlayerObserver observer) {
		observers.add(observer);
	}
	
	public int getNumOfMoves() {
		return numOfMoves;
	}

	public boolean isWinner() {
		return winner;
	}

	public String getCharac() {
		return charac;
	}

	public boolean isTurn() {
		return turn;
	}

	public void addNumOfMoves() {
		this.numOfMoves += 1;
	}

	public void setWinner(boolean winner) {
		if(!this.winner) {
			this.winner = winner;
			notifyObservers(PlayerEvent.WIN);
		}
	}


	public void setTurn(boolean turn) {
		if(this.turn) notifyObservers(PlayerEvent.TURN);
		this.turn = turn;
	}
	
	private void notifyObservers(PlayerEvent event) {
		observers.stream()
			.forEach(ob -> ob.occurredEvent(this, event));
	}
	
}
