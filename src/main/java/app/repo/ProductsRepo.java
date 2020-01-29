package app.repo;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Products;

public interface ProductsRepo extends  JpaRepository<Products, Long> {
        Page<Products> findAll(Pageable pageable);
}
