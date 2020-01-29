package app.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.model.Order;
import app.model.User;

@Service
public class OrderService {

    @Autowired
    private OrderRepo OrderRepo;

    @Autowired
    private UserRepository userRepo;

    public Iterable<Order> findAll() {
        return OrderRepo.findAll();
    }
    
    public List<Order> findAllList() {
        return OrderRepo.findAll();
    }
    
    public Page<Order> findAll(int p) {
        Pageable pa=PageRequest.of(p, 5);
        return OrderRepo.findAll(pa);
    }

    public Page<Order> findByUserId(Long userId,int p) {

        Page<Order> page;
        Pageable pa=PageRequest.of(p,5);
        page = OrderRepo.findByUserId(userId,pa);
        return page;
    }

    public Iterable<Order> findByUserId(Long userId) {
        User user = userRepo.findById(userId).get();
        return OrderRepo.findByUserId(user.getId());
    }

    public Order findById(Long id) {
    	Order order = OrderRepo.findById(id).get();
        return order;
    }

    public void update(Order order, Long userId) {
        User user = userRepo.findById(userId).get();
        order.setUser(user);

        OrderRepo.save(order);
    }

    public void save(Order order, Long userId) {
        User user = userRepo.findById(userId).get();
        Order mess = new Order(order);
        mess.setUser(user);
        OrderRepo.save(mess);

    }

}
