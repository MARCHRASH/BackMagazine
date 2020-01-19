package com.magazine.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.magazine.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	List<Product> findByName(String Name);
	Product findByNameAndPrice(String name, double price);
}
