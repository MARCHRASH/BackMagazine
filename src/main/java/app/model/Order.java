package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
	String productName;
	int counterProduct;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
	
	public Order() {

    }

    public Order(String productName, int counter) {
        this.productName = productName;
        this.counterProduct = counter;
    }

    public Order(Order order) {
    	this.productName = order.productName;
        this.counterProduct = order.counterProduct;
        this.user = order.user;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCounterProduct() {
		return counterProduct;
	}

	public void setCounterProduct(int counterProduct) {
		this.counterProduct = counterProduct;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
