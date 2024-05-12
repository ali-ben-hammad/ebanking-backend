package ma.enset.ebankingBackend.repositories;

import ma.enset.ebankingBackend.entities.BankAccount;
import ma.enset.ebankingBackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends
        JpaRepository<BankAccount,String> {
    List<BankAccount> findByCustomerId(Long id);
}
