package ma.enset.ebankingBackend.web;


import lombok.AllArgsConstructor;
import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.CustomerDTO;
import ma.enset.ebankingBackend.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class CustomerRestController {

    CustomerService customerService;


    @GetMapping("/customers/search")
    public List<CustomerDTO> getCustomers(@RequestParam(name = "keyword", required = false) String keyword) {
        System.out.println(customerService.getCustomers(keyword));
        return customerService.getCustomers(keyword);
    }

    @PostMapping("/customers/create")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        return customerService.getCustomer(id);
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(id);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        customerService.deleteCustomer(id);
    }

}
