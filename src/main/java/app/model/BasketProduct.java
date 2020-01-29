package app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasketProduct {
	

	@JsonProperty("idprod")
	Long idprod;
	
	@JsonProperty("nameprod")
    String nameprod;
	
	@JsonProperty("priceprod")
    double priceprod;
	
	@JsonProperty("counterprod")
    int counterprod;
    
    public BasketProduct() {

    }

    public BasketProduct(Long id, String nameProd, double priceProd, int counterProd) {
    	this.idprod = id;
        this.nameprod = nameProd;
        this.priceprod = priceProd;
        this.counterprod = counterProd;
    }

    public BasketProduct(BasketProduct baskProd) {
    	this.idprod = baskProd.getId();
    	this.nameprod = baskProd.getNameProd();
        this.priceprod = baskProd.getPriceProd();
        this.counterprod = baskProd.getCounterProd();
    }

	public Long getId() {
		return idprod;
	}

	public void setId(Long id) {
		this.idprod = id;
	}

	public String getNameProd() {
		return nameprod;
	}

	public void setNameProd(String nameProd) {
		this.nameprod = nameProd;
	}

	public double getPriceProd() {
		return priceprod;
	}

	public void setPriceProd(double priceProd) {
		this.priceprod = priceProd;
	}

	public int getCounterProd() {
		return counterprod;
	}

	public void setCounterProd(int counterProd) {
		this.counterprod = counterProd;
	}
}
