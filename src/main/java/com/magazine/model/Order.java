package com.magazine.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "userid")
	private int userid;
	
	@Column(name = "productid")
	private int productid;
	
	@Column(name = "counter")
	private int counter;
	
	public Order() {
	}

	public Order(int userid, int productid, int counter) {
		this.id = id;
		this.userid = userid;
		this.productid = productid;
		this.counter = counter;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	@Override
	public String toString() {
		return "Order [ id="+id+"	userid="+userid+"	productid="+productid+"		counter="+counter+" ]";
	}
}
