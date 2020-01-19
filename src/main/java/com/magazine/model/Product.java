package com.magazine.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private double price;
	
	@Column(name = "counter")
	private int counter;

	public Product() {
	}

	public Product(String name, double price, int counter) {
		this.name = name;
		this.price = price;
		this.counter = counter;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void incCounter() {
		this.counter++;
	}
	
	public void decCounter() {
		this.counter--;
	}

	@Override
	public String toString() {
		return "Pilot [id=" + id + ", name=" + name + ", price=" + price + ", counter=" + counter + "]";
	}
}

