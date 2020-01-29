package app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.message.response.ResponseMessage;
import app.model.BasketProduct;
import app.model.Order;
import app.model.Products;
import app.repo.OrderService;
import app.repo.ProductsService;
import app.repo.RoleRepository;
import app.repo.UserRepository;
import app.security.jwt.JwtProvider;
import app.security.services.UserPrinciple;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class OrderController {
	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    OrderService orderServ;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
    
    @Autowired
    ProductsService prodServ;

    private Long userId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (((UserPrinciple) auth.getPrincipal()).getId() != null) {
            return ((UserPrinciple) auth.getPrincipal()).getId();
        }
        return null;
    }

    @GetMapping("/order/{page}")
    public Page<Order> orders(@PathVariable int page) {
        try {
            Page<Order> order = orderServ.findByUserId(userId(), page);
            return order;
        } catch (Exception e) {
            return null;
        }

    }
    
    @GetMapping(value = "/orders/{id}")
	public Order changeOrder(@PathVariable("id") long id) {

    	Order orderData = orderServ.findById(id);
		if (orderData!=null) {
			System.out.println("I send : "+orderData.toString());
			return orderData;
		}
		else
		{
			return null;
		}
	}
    
    @PostMapping(value = "/order/save")
    public ResponseEntity<?> saveOrders() {
    	List<Order> orders = orderServ.findAllList();
    	XSSFWorkbook wb = new XSSFWorkbook();
    	XSSFSheet sheet = wb.createSheet("Заказы");
    	sheet.setColumnWidth(0, 4000);
    	sheet.setColumnWidth(1, 6000);
    	sheet.setColumnWidth(2, 6000);
    	sheet.setColumnWidth(3, 4100);
    	XSSFRow rowHeader = sheet.createRow(0);
    	XSSFCell headerCell = rowHeader.createCell(0);
    	headerCell.setCellValue("Id заказа");
    	headerCell = rowHeader.createCell(1);
    	headerCell.setCellValue("Название продукта");
    	headerCell = rowHeader.createCell(2);
    	headerCell.setCellValue("Купленное количество");
    	headerCell = rowHeader.createCell(3);
    	headerCell.setCellValue("Пользователь");
    	for(int i=0;i<orders.size();i++) {
    		XSSFRow row = sheet.createRow(i+1);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(orders.get(i).getId());
    		cell = row.createCell(1);
    		cell.setCellValue(new XSSFRichTextString(orders.get(i).getProductName()));
    		cell= row.createCell(2);
    		cell.setCellValue(orders.get(i).getCounterProduct());
    		cell = row.createCell(3);
    		cell.setCellValue(new XSSFRichTextString(orders.get(i).getUser().getName()));
    	}
    	try {
    		File dir = new File("D:\\database\\orders.xlsx");
    		FileOutputStream fileOut = new FileOutputStream(dir);
    	    wb.write(fileOut);
    	    fileOut.close();
    	}
    	catch (Exception e) {
    	    System.out.println("Ошибка записи");
    	    return new ResponseEntity<>(new ResponseMessage("Ошибка печати."), HttpStatus.OK);
    	}
    	return new ResponseEntity<>(new ResponseMessage("Печать завершена."), HttpStatus.OK);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<?> add(@Valid @RequestBody BasketProduct basketProduct) {
    	System.out.println("Передаётся объект : "+ basketProduct.getId() +" "+basketProduct.getNameProd()+" "+basketProduct.getPriceProd()+" "+basketProduct.getCounterProd());
    	Products product;
		if((product  = prodServ.findById(basketProduct.getId()))!=null) {
    		if(product.getCounter()-basketProduct.getCounterProd()>=0) {
    			Order _order = new Order(product.getName(), basketProduct.getCounterProd());
    			orderServ.save(_order, userId());
    			int count = product.getCounter() - basketProduct.getCounterProd();
    			product.setCounter(count);
    			prodServ.update(product);
    			return new ResponseEntity<>(new ResponseMessage("Заказ добавлен"), HttpStatus.OK);
    		}
    	}
        return new ResponseEntity<>(new ResponseMessage("Заказ не добавлен"), HttpStatus.OK);
    }
    
    @GetMapping("/admin/{page}")
    public Page<Order> adminpage(@PathVariable int page) {
    	System.out.println("Получаю запрос");
        Page<Order> order = orderServ.findAll(page);
        System.out.println("Передаю данные");
        for(Order ord: order) {
        	System.out.println("Order id = "+ord.getId()+" nameProduct = "+ord.getProductName()+" counterProduct = "+ord.getCounterProduct()+" userId = "+ord.getUser());
        }
        return order;
    }
}
