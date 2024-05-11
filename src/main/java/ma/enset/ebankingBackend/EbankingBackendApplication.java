package ma.enset.ebankingBackend;

import ma.enset.ebankingBackend.entities.AccountOperation;
import ma.enset.ebankingBackend.entities.CurrentAccount;
import ma.enset.ebankingBackend.entities.Customer;
import ma.enset.ebankingBackend.entities.SavingAccount;
import ma.enset.ebankingBackend.enums.AccountStatus;
import ma.enset.ebankingBackend.enums.OperationType;
import ma.enset.ebankingBackend.repositories.AccountOperationRepository;
import ma.enset.ebankingBackend.repositories.AccountRepository;
import ma.enset.ebankingBackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            AccountRepository accountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Ali", "Mohamed", "Hassan", "Omar").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");

                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer ->{
                CurrentAccount account = new CurrentAccount();
                account.setId(UUID.randomUUID().toString());
                account.setBalance(Math.random() * 10000);
                account.setCustomer(customer);
                account.setStatus(Math.random() > 0.5 ? AccountStatus.ACTIVE : AccountStatus.CLOSED);
                account.setOverDraft(Math.random() * 1000);
                accountRepository.save(account);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 10000);
                savingAccount.setCustomer(customer);
                savingAccount.setStatus(Math.random() > 0.5 ? AccountStatus.ACTIVE : AccountStatus.CLOSED);
                savingAccount.setInterestRate(Math.random() * 100);
                accountRepository.save(savingAccount);
            });

            accountRepository.findAll().forEach( acc ->{
                AccountOperation accountOperation = new AccountOperation();
                accountOperation.setAmount(Math.random() * 1000);
                accountOperation.setOperationDate(new Date());
                accountOperation.setType(Math.random() > 0.5 ? OperationType.DEPOSIT : OperationType.WITHDRAWAL);
                accountOperation.setBankAccount(acc);

                accountOperationRepository.save(accountOperation);
            });


        };
    }

}
