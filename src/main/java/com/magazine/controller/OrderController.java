package com.magazine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.magazine.model.BasketProduct;
import com.magazine.model.Order;
import com.magazine.model.Product;
import com.magazine.model.User;
import com.magazine.repository.OrderRepository;
import com.magazine.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	OrderRepository repository;
	
	@Autowired
	ProductRepository repositoryProduct;
	
	User user = new User();
	
	private List<BasketProduct> basket = new ArrayList<BasketProduct>();
	
	@PostMapping(value = "/sendid")
	public ResponseEntity sendId(@RequestBody int id) {
		user.setId(id);
		System.out.println("Установлено id user : "+user.getId());
		return ResponseEntity.ok(true);
	}
	
	@PostMapping(value = "/sendrole")
	public ResponseEntity sendRole(@RequestBody String role) {
		user.setRole(role);
		System.out.println("Установлено id user : "+user.getRole());
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/intobasket")
	public ResponseEntity intoBasket(@RequestBody Product product) {
		System.out.println("Получаю продукт с id : "+ product.getId()+" и названием : "+product.getName()+" и количеством : "+product.getCounter());
		if(product.getCounter()!=0) {
			BasketProduct basketProduct;
			boolean check = false;
			for(BasketProduct baskProd : basket) {
				if(baskProd.getId()==product.getId()) {
					check=true;
					baskProd.incCounter();
					product.decCounter();
					Product productForChange = repositoryProduct.save(product);
					break;
				}
			}
			if(check==false) {
				basketProduct = new BasketProduct(product.getId(), product.getName());
				basketProduct.incCounter();
				basket.add(basketProduct);
				product.decCounter();
				Product productForChange = repositoryProduct.save(product);
			}
			for(BasketProduct baskProd: basket) {
				System.out.println(baskProd.getId()+"	"+baskProd.getName()+"		"+baskProd.getCounter());
			}
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok("Нет продукта на складе");
	}
	
	@PostMapping("/outbasket")
	public List<BasketProduct> outBasket(@RequestBody BasketProduct basketProduct) {
		System.out.println("Выношу продукт : "+basketProduct.getId()+"   "+basketProduct.getName()+"   "+basketProduct.getCounter());
		for(int i=0;i<basket.size();i++) {
			if(basket.get(i).getId()==basketProduct.getId()) {
				Optional<Product> productFind = repositoryProduct.findById(basket.get(i).getId());
				Product product = productFind.get();
				int counter = product.getCounter();
				counter = counter + basket.get(i).getCounter();
				product.setCounter(counter);
				product.setId(basket.get(i).getId());
				Product productForChange = repositoryProduct.save(product);
				basket.remove(i);
			}
		}
		for(BasketProduct baskProd: basket) {
			System.out.println(baskProd.getId()+"	"+baskProd.getName()+"		"+baskProd.getCounter());
		}
		return basket;
	}
	
	@GetMapping("/product/whenexit")
	public ResponseEntity whenExit() {
		for(BasketProduct baskProd : basket) {
			Optional<Product> productFind = repositoryProduct.findById(baskProd.getId());
			Product product = productFind.get();
			int counter = product.getCounter();
			counter = counter + baskProd.getCounter();
			product.setCounter(counter);
			product.setId(baskProd.getId());
			Product productForChange = repositoryProduct.save(product);
		}
		basket.clear();
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("/basket")
	public List<BasketProduct> getProductsFromBasket() {
		System.out.println("Высылаю корзину пользователя : "+user.getId());
		return basket;
	}
	
	@PostMapping("/confirmedorder")
	public List<BasketProduct> confirmedOrder(@RequestBody BasketProduct basketProduct){
		for(int i = 0;i<basket.size();i++) {
			if(basketProduct.getId()==basket.get(i).getId()) {
				Order order = repository.findByProductidAndUserid((int)basketProduct.getId(), user.getId());
				if(order!=null){
					System.out.println("I take : "+order);
					int counter = order.getCounter() + basketProduct.getCounter();
					order.setCounter(counter);
					Order _order = repository.save(order);
					basket.remove(i);
				} else {
					System.out.println("Сохраняю новый заказ для пользователя : "+user.getId());
					Order _order = repository.save(new Order(user.getId(), (int)basketProduct.getId(), basketProduct.getCounter()));
					System.out.println("Новый заказ : "+_order.getId()+" "+ _order.getUserid()+" "+ _order.getProductid()+" "+ _order.getCounter());
					basket.remove(i);
				}
			}
		}
		return basket;
	}
	
	@GetMapping("/orders")
	public Page<Order> getAllOrders(@RequestParam(name="page", defaultValue="0") int page) {
		System.out.println("Get Page Orders...");
		System.out.println("Id запросившего юзера : "+user.getId());
		if(user.getRole().equals("admin")) {
			return repository.findAll(PageRequest.of(page, 8));
		}
		return repository.findByUserid(user.getId(), PageRequest.of(page, 8));
	}
	
	@PostMapping(value = "/orders/create")
	public Order postOrder(@RequestBody Order order) 
	{
		Order _order = repository.save(new Order(order.getUserid(), order.getProductid(), order.getCounter()));
		return _order;
	}
	
	@GetMapping(value = "/orders/fulldoc/{id}")
	public Order fullOrder(@PathVariable("id") long id) {

		Optional<Order> orderData = repository.findById(id);
		if (orderData.isPresent()) {
			Order _order = orderData.get();
			_order.setUserid(orderData.get().getUserid());
			_order.setProductid(orderData.get().getProductid());
			_order.setCounter(orderData.get().getCounter());
			System.out.println("I send : "+_order.toString());
			return _order;
		}
		else
		{
			return null;
		}
	}
	
	@GetMapping(value = "/orders/change/{id}")
	public Order changeOrder(@PathVariable("id") long id) {

		Optional<Order> orderData = repository.findById(id);
		if (orderData.isPresent()) {
			Order _order = orderData.get();
			_order.setUserid(orderData.get().getUserid());
			_order.setProductid(orderData.get().getProductid());
			_order.setCounter(orderData.get().getCounter());
			System.out.println("I send : "+_order.toString());
			return _order;
		}
		else
		{
			return null;
		}
	}
	
	@PostMapping(value = "/orders/changeOrd/{id}")
	public Order postChangeOrder(@PathVariable("id") long id, @RequestBody Order order) {
		System.out.println("I take : "+order);
		order.setId(id);
		Order _order = repository.save(order);
		return _order;
	}
	
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable("id") long id) {
		System.out.println("Delete Order with ID = " + id + "...");
		repository.deleteById(id);
		return new ResponseEntity<>("Order has been deleted!", HttpStatus.OK);
	}
}
