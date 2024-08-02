package tech.theraven.customers_api.service;

import jakarta.persistence.EntityNotFoundException;
import tech.theraven.customers_api.model.Customer;

import java.util.List;

/**
 * Interface defining the service operations for managing customers.
 */
public interface CustomerService {

    /**
     * Creates a new customer.
     *
     * @param customer the customer to create
     * @return the created customer
     */
    Customer create(Customer customer);

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the ID of the customer to retrieve
     * @return the customer with the specified ID
     * @throws EntityNotFoundException if no customer with the specified ID is found
     */
    Customer getById(Long id) throws EntityNotFoundException;

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers
     */
    List<Customer> getAllCustomers();

    /**
     * Updates an existing customer.
     *
     * @param id       the ID of the customer to update
     * @param customer the customer data to update
     * @return the updated customer
     * @throws EntityNotFoundException if no customer with the specified ID is found
     */
    Customer update(Long id, Customer customer) throws EntityNotFoundException;

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     * @throws EntityNotFoundException if no customer with the specified ID is found
     */
    void delete(Long id) throws EntityNotFoundException;
}
