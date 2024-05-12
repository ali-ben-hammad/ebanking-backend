package ma.enset.ebankingBackend.web;


import lombok.AllArgsConstructor;
import ma.enset.ebankingBackend.Exceptions.CustomerNotFoundException;
import ma.enset.ebankingBackend.dtos.CustomerDTO;
import ma.enset.ebankingBackend.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
public class CustomerRestController {

    CustomerService customerService;


    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
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
    public CustomerDTO updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(id);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        customerService.deleteCustomer(id);
    }

}
