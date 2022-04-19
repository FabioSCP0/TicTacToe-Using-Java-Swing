package com.github.FabioSCP0.TicTacToe.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
	
	//position x
	private int line;
	//position y
	private int column;
	
	private String charac;
	//Has the field been marked?
	private boolean marcked = false;

	private List<FieldObserver> observers = new ArrayList<>();
	
	//Class constructor
	Field(int line, int column, String carac){
		this.line = line;
		this.column = column;
		this.charac = carac;
	}
	
	public void registerObserver(FieldObserver observer) {
		observers.add(observer);
	}
	
	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public String getCharac() {
		return charac;
	}

	public boolean isMarcked() {
		return marcked;
	}
 
	public void setLine(int line) {
		this.line = line;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setCharac(String charac) {
		this.charac = charac;
	}

	public void setMarcked(boolean marcked) {
		if(!this.marcked) {
			this.marcked = marcked;
			notifyObservers(FieldEvent.MARCAR);
		}else notifyObservers(FieldEvent.PERDEUVEZ);
	}
	
	private void notifyObservers(FieldEvent event) {
		observers.stream()
			.forEach(ob -> ob.occurredEvent(this, event));
	}
	
	void restart() {
		this.marcked = false;
	}

}
