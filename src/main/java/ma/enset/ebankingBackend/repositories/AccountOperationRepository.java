package ma.enset.ebankingBackend.repositories;

import ma.enset.ebankingBackend.entities.AccountOperation;
import ma.enset.ebankingBackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends
        JpaRepository<AccountOperation,Long> {
}
