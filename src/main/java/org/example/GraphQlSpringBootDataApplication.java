package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * First run "mvn clean package" to generate the "QCustomer" class file in "target/generated-sources-java/.."
 * <p></p>
 * The idea behind this tutorial is that you don't even need to create explicit Java methods to get data.
 * All you need is to create an empty repository class (with "@GraphQlRepository" annotation)
 * and then you can specify any Graphql methods (CRUD) in the schema.graphqls file as you need.
 */
@SpringBootApplication
public class GraphQlSpringBootDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphQlSpringBootDataApplication.class, args);
    }

}
