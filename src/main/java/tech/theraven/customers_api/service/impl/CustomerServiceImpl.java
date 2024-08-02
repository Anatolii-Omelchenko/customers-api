package tech.theraven.customers_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.theraven.customers_api.exceptions.custom.EntityAlreadyExistsException;
import tech.theraven.customers_api.exceptions.custom.EntityNotFoundException;
import tech.theraven.customers_api.model.Customer;
import tech.theraven.customers_api.repository.CustomerRepository;
import tech.theraven.customers_api.service.CustomerService;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of the service for managing customer-related operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        var customerEmail = customer.getEmail();
        if (customerRepository.existsByEmail(customerEmail)) {
            throw new EntityAlreadyExistsException(Customer.class.getSimpleName(), "Email: " + customerEmail);
        }

        customer.setCreated(getCurrentEpochTime());
        customer.setUpdated(getCurrentEpochTime());

        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class.getSimpleName(), "Id: " + id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer update(Long id, Customer customer) {
        var customerToUpdate = getById(id);

        customerToUpdate.setFullName(customer.getFullName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setPhone(customer.getPhone());
        customerToUpdate.setUpdated(getCurrentEpochTime());

        return customerToUpdate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var customerToDelete = getById(id);
        customerToDelete.setIsActive(false);
    }

    private Long getCurrentEpochTime() {
        return Instant.now().getEpochSecond();
    }
}
