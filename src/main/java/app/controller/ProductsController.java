package app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import app.message.response.ResponseMessage;
import app.model.Order;
import app.model.Products;
import app.repo.ProductsService;
import app.repo.RoleRepository;
import app.repo.UserRepository;
import app.security.jwt.JwtProvider;
import app.security.services.UserPrinciple;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class ProductsController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ProductsService prodServ;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
    
    @GetMapping(value = "/products/{id}")
	public Products changeProduct(@PathVariable("id") long id) {

		Products productData = prodServ.findById(id);
		if (productData!=null) {
			System.out.println("I send : "+productData.toString());
			return productData;
		}
		else
		{
			return null;
		}
	}
    
    @PostMapping(value = "/products/save")
    public ResponseEntity<?> saveProducts() {
    	List<Products> products = prodServ.findAllList();
    	XSSFWorkbook wb = new XSSFWorkbook();
    	XSSFSheet sheet = wb.createSheet("Остатки");
    	sheet.setColumnWidth(0, 4000);
    	sheet.setColumnWidth(1, 6000);
    	sheet.setColumnWidth(2, 4100);
    	sheet.setColumnWidth(3, 6000);
    	XSSFRow rowHeader = sheet.createRow(0);
    	XSSFCell headerCell = rowHeader.createCell(0);
    	headerCell.setCellValue("Id товара");
    	headerCell = rowHeader.createCell(1);
    	headerCell.setCellValue("Название товара");
    	headerCell = rowHeader.createCell(2);
    	headerCell.setCellValue("Цена товара");
    	headerCell = rowHeader.createCell(3);
    	headerCell.setCellValue("Осталось на складе");
    	for(int i=0;i<products.size();i++) {
    		XSSFRow row = sheet.createRow(i+1);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(products.get(i).getId());
    		cell = row.createCell(1);
    		cell.setCellValue(new XSSFRichTextString(products.get(i).getName()));
    		cell = row.createCell(2);
    		cell.setCellValue(products.get(i).getPrice());
    		cell = row.createCell(3);
    		cell.setCellValue(products.get(i).getCounter());
    	}
    	try {
    		File dir = new File("D:\\database\\products.xlsx");
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
    
    @DeleteMapping("/products/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
		System.out.println("Delete Product with ID = " + id + "...");
		prodServ.deleteById(id);
		return new ResponseEntity<>("Product has been deleted!", HttpStatus.OK);
	}
	
	@PostMapping(value = "/products/changePro/{id}")
	public Products postChangeProduct(@PathVariable("id") long id, @RequestBody Products product) {
		System.out.println("I take : "+product.getId()+" "+product.getName()+" "+product.getPrice()+" "+product.getCounter());
		System.out.println("And i take id = "+id);
		product.setId(id);
		Products _product = prodServ.update(product);
		return _product;
	}

    @GetMapping("/home/{page}")
    public Page<Products> documents(@PathVariable int page) {
        try {
            Page<Products> prod = prodServ.findAll(page);
            return prod;
        } catch (Exception e) {
            return null;
        }

    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> add(@Valid @RequestBody Products prod) {
    	Products products = new Products(prod.getName(), prod.getPrice(), prod.getCounter());
    	prodServ.save(products);
        return new ResponseEntity<>(new ResponseMessage("Products added!"), HttpStatus.OK);
    }
}