package com.finalproject.dataplatform.controller;

import com.finalproject.dataplatform.dto.requests.CustomerRequestDto;
import com.finalproject.dataplatform.dto.responses.CustomerResponseDto;
import com.finalproject.dataplatform.model.Customer;
import com.finalproject.dataplatform.repository.CustomerRepository;
import com.finalproject.dataplatform.service.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{customerId}")
    public CustomerResponseDto getCustomer(@PathVariable String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.map(CustomerMapper::toDTO).orElse(null);
    }

    @PostMapping
    public CustomerResponseDto addCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        Customer customer = CustomerMapper.toEntity(customerRequestDto);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toDTO(saved);
    }

    @PutMapping("/{customerId}")
    public CustomerResponseDto updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequestDto customerRequestDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = CustomerMapper.toEntity(customerRequestDto);
            customer.setCustomerId(customerId);
            Customer updated = customerRepository.save(customer);
            return CustomerMapper.toDTO(updated);
        }
        return null;
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable String customerId) {
        customerRepository.deleteById(customerId);
    }
}
