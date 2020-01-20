package com.magazine.controller;

import java.util.Optional;

//import static java.nio.charset.StandardCharset.UTF_8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magazine.model.Product;
import com.magazine.model.User;
import com.magazine.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductRepository repository;
	
	@PostMapping(value = "/products/check")
	public ResponseEntity check(@RequestBody Product product) {
		if(repository.findByNameAndPrice(product.getName(), product.getPrice())!=null) {
			return ResponseEntity.ok(false);
		}
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("/products")
	public Page<Product> getAllProducts(@RequestParam(name="page", defaultValue="0") int page) {
		System.out.println("Get Page Products...");
		return repository.findAll(PageRequest.of(page, 8));
	}
	
	@PostMapping(value = "/products/create")
	public Product postProduct(@RequestBody Product product) 
	{
		Product _product = repository.save(new Product(product.getName(), product.getPrice(), product.getCounter()));
		return _product;
	}
	
	@GetMapping(value = "/products/fulldoc/{id}")
	public Product fullProduct(@PathVariable("id") long id) {

		Optional<Product> productData = repository.findById(id);
		if (productData.isPresent()) {
			Product _product = productData.get();
			_product.setName(productData.get().getName());
			_product.setPrice(productData.get().getPrice());
			_product.setCounter(productData.get().getCounter());
			System.out.println("I send : "+_product.toString());
			return _product;
		}
		else
		{
			return null;
		}
	}
	
	@GetMapping(value = "/products/change/{id}")
	public Product changeProduct(@PathVariable("id") long id) {

		Optional<Product> productData = repository.findById(id);
		if (productData.isPresent()) {
			Product _product = productData.get();
			_product.setName(productData.get().getName());
			_product.setPrice(productData.get().getPrice());
			_product.setCounter(productData.get().getCounter());
			System.out.println("I send : "+_product.toString());
			return _product;
		}
		else
		{
			return null;
		}
	}
	
	@PostMapping(value = "/products/changePro/{id}")
	public Product postChangeProduct(@PathVariable("id") long id, @RequestBody Product product) {
		System.out.println("I take : "+product);
		product.setId(id);
		Product _product = repository.save(product);
		return _product;
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
		System.out.println("Delete Product with ID = " + id + "...");
		repository.deleteById(id);
		return new ResponseEntity<>("Product has been deleted!", HttpStatus.OK);
	}
}
