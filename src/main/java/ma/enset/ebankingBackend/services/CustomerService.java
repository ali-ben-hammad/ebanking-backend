package ma.enset.ebankingBackend.services;

import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.CustomerDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {


    List<CustomerDTO> getCustomers();

    public CustomerDTO createCustomer(CustomerDTO customerDTO);

    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;


    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;

    @Transactional
    public void deleteCustomer(Long id) throws CustomerNotFoundException;

}
