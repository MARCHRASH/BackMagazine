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
	
	/*@GetMapping("/pilots/pilot")
	public List<Pilot> getPagePilotsPilotSort(@RequestParam(name="page", defaultValue="0") int page, @RequestParam(name="field", defaultValue="name") String field) {
		System.out.println("Get Page Sort Pilots...");
		ArrayList<Pilot> pilots = new ArrayList<Pilot>();
		pilots = new ArrayList<>(repository.findAll(PageRequest.of(page, 8)).getContent());
		switch(field)
		{
			case "name" :
			{
				Collections.sort(pilots, Comparator.comparing(Pilot::getName));
				break;
			}
		}
		return pilots;
	}*/

	/*@PostMapping("/pilot")
	public Pilot postPilot(@RequestBody Pilot pilot) {

		Pilot _pilot = repository.save(new Pilot(pilot.getName(), pilot.getSurname(), pilot.getSecondname(), pilot.getPhone()));
		return _pilot;
	}

	@DeleteMapping("/pilot/{id}")
	public ResponseEntity<String> deletePilot(@PathVariable("id") long id) {
		System.out.println("Delete Pilot with ID = " + id + "...");

		repository.deleteById(id);

		return new ResponseEntity<>("Pilot has been deleted!", HttpStatus.OK);
	}

	/*@GetMapping("customers/age/{age}")
	public List<Customer> findByAge(@PathVariable int age) {

		List<Customer> customers = repository.findByAge(age);
		return customers;
	}*/

	/*@PutMapping("/pilot/{id}")
	public ResponseEntity<Pilot> updatePilot(@PathVariable("id") long id, @RequestBody Pilot pilot) {
		System.out.println("Pilot Customer with ID = " + id + "...");

		Optional<Pilot> pilotData = repository.findById(id);

		if (pilotData.isPresent()) {
			Pilot _pilot = pilotData.get();
			_pilot.setName(pilot.getName());
			_pilot.setSurname(pilot.getSurname());
			_pilot.setSecondname(pilot.getSecondname());
			_pilot.setPhone(pilot.getPhone());
			return new ResponseEntity<>(repository.save(_pilot), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}*/
}
