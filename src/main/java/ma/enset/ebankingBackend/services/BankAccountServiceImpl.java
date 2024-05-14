package ma.enset.ebankingBackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingBackend.Exceptions.AccountNotFoundException;
import ma.enset.ebankingBackend.Exceptions.BalanceNotSufficientException;
import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.*;
import ma.enset.ebankingBackend.entities.*;
import ma.enset.ebankingBackend.enums.OperationType;
import ma.enset.ebankingBackend.mappers.BankAccountMapper;
import ma.enset.ebankingBackend.repositories.AccountOperationRepository;
import ma.enset.ebankingBackend.repositories.AccountRepository;
import ma.enset.ebankingBackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private AccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapper dtoMapper;
    @Override
    public SavingAccountDTO createSavingAccount(String id, double balance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer =  customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        savingAccountDTO.setId(id);
        savingAccountDTO.setBalance(balance);
        savingAccountDTO.setInterestRate(interestRate);
        savingAccountDTO.setCreatedAt(new Date());
        savingAccountDTO.setCustomer(dtoMapper.fromCustomer(customer));
        bankAccountRepository.save(dtoMapper.fromSavingAccountDTO(savingAccountDTO));
        return savingAccountDTO;
    }

    @Override
    public CurrentAccountDTO createCurrentAccount(String id, double balance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer =  customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        currentAccountDTO.setId(id);
        currentAccountDTO.setBalance(balance);
        currentAccountDTO.setOverDraft(overDraft);
        currentAccountDTO.setCreatedAt(new Date());
        currentAccountDTO.setCustomer(dtoMapper.fromCustomer(customer));
        bankAccountRepository.save(dtoMapper.fromCurrentAccountDTO(currentAccountDTO));
        return currentAccountDTO;
    }

    @Override
    public BankAccountDTO getBankAccount(String id) throws AccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
    }



    @Override
    public void debit(String accountId, double amount, String description) throws AccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundException("BankAccount not found"));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.WITHDRAWAL);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws AccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEPOSIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws AccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public Page<BankAccountDTO> bankAccountList(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(pageable);
        Page<BankAccountDTO> bankAccountDTOS = bankAccounts.map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        });
        return bankAccountDTOS;
    }
    @Override
    public Page<AccountOperationDTO> accountHistory(String accountId, int page , int size){
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,PageRequest.of(page, size));
        return accountOperations.map(op->dtoMapper.fromAccountOperation(op));
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws AccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new AccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }
}
