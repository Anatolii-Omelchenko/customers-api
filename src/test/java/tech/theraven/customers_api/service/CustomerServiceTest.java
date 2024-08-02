package tech.theraven.customers_api.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.theraven.customers_api.exceptions.custom.EntityAlreadyExistsException;
import tech.theraven.customers_api.exceptions.custom.EntityNotFoundException;
import tech.theraven.customers_api.testutils.FakeDataGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = NONE)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private final int TOTAL_CUSTOMERS = 10;

    @Test
    void testCreateCustomer() {
        // Prepare
        var customer = FakeDataGenerator.customerBuilder().build();

        var createdCustomer = customerService.create(customer);
        var expectedCustomer = customerService.getById(createdCustomer.getId());

        // Assert
        assertEquals(expectedCustomer, createdCustomer);
    }

    @Test
    void testCreateCustomerWhenCustomerAlreadyExists() {
        // Prepare
        var existingEmail = "oleksandr.kovalenko@example.com";
        var customer = FakeDataGenerator.customerBuilder()
                .email(existingEmail)
                .build();

        // Assert
        assertThrows(EntityAlreadyExistsException.class, () -> customerService.create(customer));
    }

    @Test
    void getAllCustomers() {
        // Prepare
        var customers = customerService.getAllCustomers();

        // Assert
        assertEquals(customers.size(), TOTAL_CUSTOMERS);
    }

    @Test
    void getCustomerById() {
        // Prepare
        var existingCustomerId = 1L;

        // Assert
        assertNotNull(customerService.getById(existingCustomerId));
    }

    @Test
    void getCustomerByIdWhenCustomerNotFound() {
        // Prepare
        var nonExistingCustomerId = 999_999L;

        // Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.getById(nonExistingCustomerId));
    }

    @Test
    void updateCustomer() {
        // Prepare
        var existingCustomerId = 1L;
        var customer = FakeDataGenerator.customerBuilder().build();

        // Execute
        var updatedCustomer = customerService.update(existingCustomerId, customer);

        // Assert
        assertEquals(updatedCustomer.getId(), existingCustomerId);
        assertEquals(updatedCustomer.getFullName(), customer.getFullName());
        assertEquals(updatedCustomer.getPhone(), customer.getPhone());
    }

    @Test
    void updateCustomerWhenCustomerNotFound() {
        // Prepare
        var nonExistingCustomerId = 999_999L;
        var customer = FakeDataGenerator.customerBuilder().build();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.update(nonExistingCustomerId, customer));
    }

    @Test
    void deleteCustomer() {
        // Prepare
        var existingCustomerId = 1L;

        // Execute
        customerService.delete(existingCustomerId);

        // Assert
        var deactivatedCustomer = customerService.getById(existingCustomerId);
        assertFalse(deactivatedCustomer.getIsActive());
    }

    @Test
    void deleteCustomerWhenCustomerNotFound() {
        // Prepare
        var nonExistingCustomerId = 999_999L;

        // Assert
        assertThrows(EntityNotFoundException.class, () -> customerService.delete(nonExistingCustomerId));
    }
}
