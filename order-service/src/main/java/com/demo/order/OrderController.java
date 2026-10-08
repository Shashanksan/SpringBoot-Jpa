package com.demo.order;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody Order o)
    { return service.create(o); }

    @GetMapping
    public List<Order> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Order one(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/customer/{customerId}")
    public List<Order> byCustomer(@PathVariable Long customerId) { return service.findByCustomer(customerId); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
