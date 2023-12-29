package org.example.repository;

import org.example.model.Customer;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String>,
        ReactiveQuerydslPredicateExecutor<Customer> {
}
