package com.magazine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.magazine.model.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
	Page<Order> findByUserid(int userid, Pageable pageable);
	Order findByProductid(int productid);
	Order findByProductidAndUserid(int productid, int userid);
}
