package com.demo.order;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final RestClient customerClient;

    public OrderService(OrderRepository repo, RestClient customerClient) {
        this.repo = repo;
        this.customerClient = customerClient;
    }

    /** Calls customer-service (microservice to microservice) to be sure the customer exists, then saves the order. */
    @Transactional
    public Order create(Order order) {
        customerClient.get()
                .uri("/api/customers/{id}", order.getCustomerId())
                .retrieve()
                .onStatus(s -> s.value() == 404, (req, res) -> {
                    throw new NotFoundException("Customer not found: " + order.getCustomerId());
                })
                .body(CustomerDto.class);
        order.setId(null);
        return repo.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomer(Long customerId) { return repo.findByCustomerId(customerId); }

    @Transactional
    public void delete(Long id) { repo.delete(findById(id)); }
}
