package tech.theraven.customers_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.theraven.customers_api.mapper.CustomerMapper;
import tech.theraven.customers_api.model.dto.CustomerInfo;
import tech.theraven.customers_api.model.dto.CustomerCreateDTO;
import tech.theraven.customers_api.model.dto.CustomerUpdateDTO;
import tech.theraven.customers_api.service.CustomerService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper mapper;

    @PostMapping
    public ResponseEntity<CustomerInfo> createCustomer(@RequestBody CustomerCreateDTO customerCreateDTO) {
        var customer = mapper.toCustomer(customerCreateDTO);
        var createdCustomer = customerService.create(customer);
        var customerInfo = mapper.toCustomerInfo(createdCustomer);

        return ResponseEntity.status(CREATED).body(customerInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfo> getCustomer(@PathVariable Long id) {
        var customer = customerService.getById(id);
        var customerInfo = mapper.toCustomerInfo(customer);

        return ResponseEntity.ok(customerInfo);
    }

    @GetMapping
    public ResponseEntity<List<CustomerInfo>> getCustomers() {
        var listOfCustomers = customerService.getAllCustomers().stream()
                .map(mapper::toCustomerInfo)
                .toList();

        return ResponseEntity.ok(listOfCustomers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerInfo> updateCustomer(@RequestBody CustomerUpdateDTO customerUpdateDTO,
                                                       @PathVariable Long id) {
        var customer = mapper.toCustomer(customerUpdateDTO);
        var updatedCustomer = customerService.update(id, customer);
        var customerInfo = mapper.toCustomerInfo(updatedCustomer);

        return ResponseEntity.ok(customerInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
