package tech.theraven.customers_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.customers_api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
