package ma.enset.ebankingBackend.mappers;

import ma.enset.ebankingBackend.dtos.CustomerDTO;
import ma.enset.ebankingBackend.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.Beans;

@Service
public class BankAccountMapper {
    public Customer fromCustomerDTO (CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public CustomerDTO fromCustomer (Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }
}
