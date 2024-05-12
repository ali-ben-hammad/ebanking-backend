package ma.enset.ebankingBackend.services;

import com.mysql.cj.log.Log;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.CustomerDTO;
import ma.enset.ebankingBackend.entities.BankAccount;
import ma.enset.ebankingBackend.entities.Customer;
import ma.enset.ebankingBackend.mappers.BankAccountMapper;
import ma.enset.ebankingBackend.repositories.AccountRepository;
import ma.enset.ebankingBackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private BankAccountMapper bankAccountMapper;



    @Override
    public List<CustomerDTO> getCustomers(){
        List<Customer> customers = customerRepository.findAll();
            List<CustomerDTO> customerDTOS = customers.stream().map(bankAccountMapper::fromCustomer).toList();
            return customerDTOS;
    }


    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO){
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
         Customer savedCustomer  =  customerRepository.save(customer);
        log.info("Customer created with id: "+savedCustomer.getId());
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
       Customer customer = customerRepository.findById(customerDTO.getId()).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
       Customer updatedCustomer = customerRepository.save(customer);
       log.info("Customer updated with id: "+updatedCustomer.getId());
       return bankAccountMapper.fromCustomer(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        // delete associated accounts
        List<BankAccount> bankAccounts = accountRepository.findByCustomerId(id);
        bankAccounts.forEach(accountRepository::delete);

        for (BankAccount bankAccount : bankAccounts) {
            accountRepository.delete(bankAccount);
        }


        log.info("Customer deleted with id: "+customer.getId());

        customerRepository.delete(customer);
    }


}
