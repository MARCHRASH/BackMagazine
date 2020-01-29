package app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Order;

public interface OrderRepo extends  JpaRepository<Order, Long> {
        Iterable<Order> findByUserId(Long user);
        Page<Order> findAll(Pageable pageable);
        Page<Order> findByUserId(Long user,Pageable pageable);
}

