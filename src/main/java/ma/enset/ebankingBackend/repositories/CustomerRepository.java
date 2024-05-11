package ma.enset.ebankingBackend.repositories;

import ma.enset.ebankingBackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends
        JpaRepository<Customer,Long> {
}
