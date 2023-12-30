package org.example.repository;

import org.example.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private HttpGraphQlTester graphQlTester;

    private Flux<Customer> customers;

    @BeforeEach
    void setUp(@Autowired ApplicationContext applicationContext) {

        if (graphQlTester == null) {

            var webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                    .configureClient()
                    .baseUrl("/graphql")
                    .build();

            graphQlTester = HttpGraphQlTester.create(webTestClient);
        }

        customers = customerRepository.deleteAll()
                .thenMany(Flux.just("Test 1", "Test 2", "Test 3", "Test 4")
                        .map(name -> new Customer(null, name))
                        .flatMap(customerRepository::save));

        customers.subscribe();
    }

    @Test
    void testCustomers() {

        String query = """
                {
                    customers {
                      id
                      name
                    }
                }
                """;

        var customersReceived = graphQlTester.document(query)
                .execute()
                .path("data.customers")
                .hasValue()
                .entityList(Customer.class)
                .get();

        var namesOfCustomers = customers.toStream().map(Customer::name).collect(Collectors.toSet());
        var namesOfCustomersReceived = customersReceived.stream().map(Customer::name).collect(Collectors.toSet());

        assertEquals(namesOfCustomers, namesOfCustomersReceived);
    }

    @Test
    void testCustomerById() {

        for (var customer : customers.toIterable()) {

            String query = """
                    {
                        customerById(id: "%s") {
                          id
                          name
                        }
                    }
                    """;

            query = String.format(query, customer.id());

            var customerReceived = graphQlTester.document(query)
                    .execute()
                    .path("data.customerById")
                    .hasValue()
                    .entity(Customer.class)
                    .get();

            assertEquals(customer.name(), customerReceived.name());
        }
    }

    @Test
    void testCustomerByIdNotFound() {

        String id = "-1";

        String query = """
                {
                    customerById(id: "%s") {
                      id
                      name
                    }
                }
                """;

        query = String.format(query, id);

        graphQlTester.document(query)
                .execute()
                .path("data.customerById")
                .valueIsNull();
    }

    @Test
    void testCustomerByName() {

        for (var customer : customers.toIterable()) {

            String query = """
                    {
                        customerByName(name: "%s") {
                          id
                          name
                        }
                    }
                    """;

            query = String.format(query, customer.name());

            var customerReceived = graphQlTester.document(query)
                    .execute()
                    .path("data.customerByName")
                    .hasValue()
                    .entity(Customer.class)
                    .get();

            assertEquals(customer.name(), customerReceived.name());
        }
    }

    @Test
    void testCustomerByNameNotFound() {

        String name = "--";

        String query = """
                {
                    customerByName(name: "%s") {
                      id
                      name
                    }
                }
                """;

        query = String.format(query, name);

        graphQlTester.document(query)
                .execute()
                .path("data.customerByName")
                .valueIsNull();
    }
}