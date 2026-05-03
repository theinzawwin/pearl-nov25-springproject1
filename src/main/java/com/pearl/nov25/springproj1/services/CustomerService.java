package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.CustomerInput;
import com.pearl.nov25.springproj1.models.Customer;
import com.pearl.nov25.springproj1.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public boolean saveCustomer (CustomerInput customerInput) {
        Customer cus = new Customer();
        cus.setName(customerInput.name());
        cus.setPhone(customerInput.phone());
        cus.setStatus(customerInput.status());
        customerRepository.save(cus);
        return true;
    }
}
