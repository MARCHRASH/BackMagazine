package app.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.model.Order;
import app.model.Products;
import app.model.User;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepo productsRepo;

    public Iterable<Products> findAll() {
        return productsRepo.findAll();
    }
    public Page<Products> findAll(int p) {
        Pageable pa=PageRequest.of(p, 5);
        return productsRepo.findAll(pa);
    }

    public Products findById(Long id) {
    	Products prod = productsRepo.findById(id).get();
        return prod;
    }

    public Products update(Products prod) {
        return productsRepo.save(prod);
    }

    public Products save(Products prod) {
        Products mess = new Products(prod);
        productsRepo.save(mess);
        return mess;

    }
	public void deleteById(long id) {
		productsRepo.deleteById(id);
	}
	public List<Products> findAllList() {
		return productsRepo.findAll();
	}

}
