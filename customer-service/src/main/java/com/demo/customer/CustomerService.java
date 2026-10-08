package com.demo.customer;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) 
    { this.repo = repo; }

    @Transactional
    public Customer create(Customer c) {
        if (repo.existsByEmail(c.getEmail())) {
            throw new DuplicateException("Email already exists: " + c.getEmail());
        }
        c.setId(null);
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

    @Transactional
    public Customer update(Long id, Customer in) {
        Customer c = findById(id);
        c.setName(in.getName());
        c.setEmail(in.getEmail());
        c.setPhone(in.getPhone());
        return repo.save(c);
    }

    @Transactional
    public void delete(Long id) {
        repo.delete(findById(id));
    }
}
