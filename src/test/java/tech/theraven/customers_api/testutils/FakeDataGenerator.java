package tech.theraven.customers_api.testutils;

import com.github.javafaker.Faker;
import lombok.Getter;
import tech.theraven.customers_api.model.Customer;
import tech.theraven.customers_api.testutils.enums.CustomerFiledName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeDataGenerator {

    private static final Faker FAKER = new Faker();

    @Getter
    private static final List<Customer> customers = new ArrayList<>();

    private FakeDataGenerator() {
    }

    static {
        customers.addAll(generateCustomers());
    }

    static List<Customer> generateCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            customers.add(customerBuilder().build());
        }
        return customers;
    }

    public static CustomerBuilder customerBuilder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private Long id;
        private String fullName = FAKER.name().fullName();
        private String email = FAKER.internet().emailAddress();
        private String phone = generatePhoneNumber();
        private Boolean isActive = Boolean.TRUE;
        private Long created = FAKER.date().birthday().getTime();
        private Long updated = FAKER.date().birthday().getTime();

        public CustomerBuilder withInvalid(CustomerFiledName... fields) {
            Arrays.stream(fields)
                    .forEach(field -> {
                        switch (field) {
                            case FULL_NAME -> this.fullName = null;
                            case EMAIL -> this.email = "invalid_email";
                            case PHONE -> this.phone = "invalid_phone";
                            case IS_ACTIVE -> this.isActive = null;
                            case CREATED -> this.created = null;
                            case UPDATED -> this.updated = null;
                        }
                    });
            return this;
        }

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CustomerBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CustomerBuilder created(Long created) {
            this.created = created;
            return this;
        }

        public CustomerBuilder updated(Long updated) {
            this.updated = updated;
            return this;
        }

        public Customer build() {
            return Customer.builder()
                    .id(this.id)
                    .fullName(this.fullName)
                    .email(this.email)
                    .phone(this.phone)
                    .isActive(this.isActive)
                    .created(this.created)
                    .updated(this.updated)
                    .build();
        }

        private String generatePhoneNumber() {
            var randomNumber = FAKER.phoneNumber().subscriberNumber(9);
            return "+380" + randomNumber;
        }
    }
}
