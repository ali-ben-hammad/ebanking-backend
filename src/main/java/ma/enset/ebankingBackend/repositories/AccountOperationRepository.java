package ma.enset.ebankingBackend.repositories;

import ma.enset.ebankingBackend.entities.AccountOperation;
import ma.enset.ebankingBackend.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends
        JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByBankAccountId(String accountId);

    Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId, PageRequest of);
}
