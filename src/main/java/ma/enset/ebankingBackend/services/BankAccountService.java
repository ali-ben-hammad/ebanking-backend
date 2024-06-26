package ma.enset.ebankingBackend.services;

import ma.enset.ebankingBackend.Exceptions.AccountNotFoundException;
import ma.enset.ebankingBackend.Exceptions.BalanceNotSufficientException;
import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.*;
import org.springframework.data.domain.Page;

public interface BankAccountService {

    public SavingAccountDTO createSavingAccount(String id, double balance, double interestRate, Long customerId) throws CustomerNotFoundException;

    public CurrentAccountDTO createCurrentAccount(String id, double balance, double overDraft, Long customerId) throws CustomerNotFoundException;

    public BankAccountDTO getBankAccount(String id) throws AccountNotFoundException;


    void debit(String accountId, double amount, String description) throws AccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws AccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws AccountNotFoundException, BalanceNotSufficientException;




    Page<BankAccountDTO> bankAccountList(int page, int size);

    Page<AccountOperationDTO> accountHistory(String accountId, int page , int size);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws AccountNotFoundException;
}
