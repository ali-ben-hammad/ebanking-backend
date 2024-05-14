package ma.enset.ebankingBackend.repositories;

import ma.enset.ebankingBackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends
        JpaRepository<Customer, Long> {
    List<Customer> findByNameContains(String keyword);
}
