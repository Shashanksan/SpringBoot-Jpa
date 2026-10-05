package com.demo.customer;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer c) { return service.create(c); }

    @GetMapping
    public List<Customer> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @Valid @RequestBody Customer c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
