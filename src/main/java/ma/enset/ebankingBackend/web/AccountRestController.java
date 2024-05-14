package ma.enset.ebankingBackend.web;


import lombok.AllArgsConstructor;
import ma.enset.ebankingBackend.Exceptions.AccountNotFoundException;
import ma.enset.ebankingBackend.Exceptions.BalanceNotSufficientException;
import ma.enset.ebankingBackend.dtos.*;
import ma.enset.ebankingBackend.services.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class AccountRestController {
    private final BankAccountService bankAccountService;

/*
    @GetMapping("/accounts/{accountId}")
    public AccountHistoryDTO getBankAccount(
            @PathVariable String accountId,
            @RequestParam(name = "pageOperations", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        BankAccountDTO account = bankAccountService.getBankAccount(accountId);
        // get the account's operations
        Page<AccountOperationDTO> operations = bankAccountService.accountHistory(accountId, page, size);

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(account.getBalance());
        accountHistoryDTO.setCurrentPage(operations.getNumber());
        accountHistoryDTO.setPageSize(operations.getSize());
        accountHistoryDTO.setTotalPages(operations.getTotalPages());
        accountHistoryDTO.setAccountOperationDTOS(operations.getContent());
        return accountHistoryDTO;

    }
*/
    @GetMapping("/accounts")
    public Page<BankAccountDTO> listAccounts(
            @RequestParam(name = "pageOperations", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return bankAccountService.bankAccountList(page, size);
    }




    @GetMapping("/accounts/{accountId}")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "pageOperations", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }


    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws AccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws AccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }


}
