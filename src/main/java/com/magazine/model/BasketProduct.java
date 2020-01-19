package com.magazine.model;

public class BasketProduct {
	private long id;
	private String name;
	private int counter;
	
	public BasketProduct() {	
	}
	
	public BasketProduct(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public void incCounter() {
		this.counter++;
	}
	
	public void decCounter() {
		this.counter--;
	}
}
