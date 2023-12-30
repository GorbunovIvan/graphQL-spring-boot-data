package org.example;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final CustomerRepository customerRepository;

    public MyApplicationRunner(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        customerRepository.deleteAll()
                .thenMany(Flux.just("Jurgen", "Josh", "Joe", "Dave", "Mark", "Yuxin", "Madhura", "Olga")
                        .map(name -> new Customer(null, name))
                        .flatMap(customerRepository::save))
                .subscribe(System.out::println);
    }
}
