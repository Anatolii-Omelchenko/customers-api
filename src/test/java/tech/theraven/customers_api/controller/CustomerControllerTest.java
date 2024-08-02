package tech.theraven.customers_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.theraven.customers_api.exceptions.custom.EntityAlreadyExistsException;
import tech.theraven.customers_api.exceptions.custom.EntityNotFoundException;
import tech.theraven.customers_api.mapper.CustomerMapper;
import tech.theraven.customers_api.mapper.CustomerMapperImpl;
import tech.theraven.customers_api.model.Customer;
import tech.theraven.customers_api.service.CustomerService;
import tech.theraven.customers_api.testutils.FakeDataGenerator;
import tech.theraven.customers_api.testutils.enums.CustomerFiledName;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.theraven.customers_api.constants.ValidationConstants.EMAIL_FORMAT_MESSAGE;
import static tech.theraven.customers_api.constants.ValidationConstants.PHONE_FORMAT_MESSAGE;
import static tech.theraven.customers_api.testutils.FakeDataGenerator.customerBuilder;
import static tech.theraven.customers_api.testutils.enums.CustomerFiledName.*;

@WebMvcTest(CustomerController.class)
@Import(CustomerMapperImpl.class)
@DisplayName("Testing CustomerController")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerMapper mapper;

    @MockBean
    private CustomerService customerService;

    private static List<Customer> customers;
    private static final String REQUEST_URI = "/api/customers";

    @BeforeAll
    static void setUp() {
        customers = FakeDataGenerator.getCustomers();
    }

    @SneakyThrows
    @DisplayName("Method createCustomer should return 201 when input data is valid")
    @Test
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() {
        // Prepare
        var customerId = 999L;
        var customer = customerBuilder()
                .id(customerId)
                .build();

        var customerToCreate = mapper.toCustomerCreateDTO(customer);

        when(customerService.create(any(Customer.class))).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(post(REQUEST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.fullName").value(customerToCreate.getFullName()))
                .andExpect(jsonPath("$.email").value(customerToCreate.getEmail()))
                .andExpect(jsonPath("$.phone").value(customerToCreate.getPhone()));
    }

    @SneakyThrows
    @DisplayName("Method createCustomer should return 201 when customer already exists")
    @Test
    void createCustomer_WhenCustomerAlreadyExists_ShouldReturnBadRequest() {
        // Prepare
        var customerId = 999L;
        var customerEmail = "customer@theraven.com";
        var customer = customerBuilder()
                .id(customerId)
                .email(customerEmail)
                .build();
        var errorMessage = Customer.class.getSimpleName() + " with 'Email: " + customerEmail + "' already exists!";

        var customerToCreate = mapper.toCustomerCreateDTO(customer);

        when(customerService.create(any(Customer.class)))
                .thenThrow(new EntityAlreadyExistsException(Customer.class.getSimpleName(), "Email: " + customerEmail));

        // Act & Assert
        mockMvc.perform(post(REQUEST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerToCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @SneakyThrows
    @DisplayName("Method createCustomer should return 400 when input data is invalid")
    @ParameterizedTest
    @MethodSource("invalidCustomerFieldsForCreate")
    void createCustomer_WithInValidData_ShouldReturnBadRequest(CustomerFiledName fieldName, String errorMessage) {
        // Prepare
        var customer = customerBuilder()
                .withInvalid(fieldName)
                .build();
        var customerToCreate = mapper.toCustomerCreateDTO(customer);

        // Act & Assert
        mockMvc.perform(post(REQUEST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerToCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).create(any(Customer.class));
    }

    public static Stream<Arguments> invalidCustomerFieldsForCreate() {
        return Stream.of(
                Arguments.of(FULL_NAME, "must not be blank"),
                Arguments.of(PHONE, PHONE_FORMAT_MESSAGE),
                Arguments.of(EMAIL, EMAIL_FORMAT_MESSAGE)
        );
    }

    @SneakyThrows
    @DisplayName("Method createCustomer should return 400 when request body is missing")
    @Test
    void createCustomer_WithEmptyBody_ShouldReturnBadRequest() {
        // Prepare
        var errorMessage = "Required request body is missing";

        // Act & Assert
        mockMvc.perform(post(REQUEST_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).create(any(Customer.class));
    }

    @SneakyThrows
    @DisplayName("Method getCustomer should return 200 when customer is present")
    @Test
    void getCustomer_WhenCustomerPresent_ShouldReturnStatusOk() {
        // Prepare
        var customerId = 999L;
        var customer = customerBuilder()
                .id(customerId)
                .build();

        when(customerService.getById(customerId)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get(REQUEST_URI + "/" + customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.fullName").value(customer.getFullName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()));
    }

    @SneakyThrows
    @DisplayName("Method getCustomer should return 404 when customer is not present")
    @Test
    void getCustomer_WhenCustomerIsNotPresent_ShouldReturnStatusNotFound() {
        // Prepare
        var customerId = 999L;
        var errorMessage = Customer.class.getSimpleName() + " with `Id: " + customerId + "` was not found!";

        when(customerService.getById(customerId))
                .thenThrow(new EntityNotFoundException(Customer.class.getSimpleName(), "Id: " + customerId));

        // Act & Assert
        mockMvc.perform(get(REQUEST_URI + "/" + customerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @SneakyThrows
    @DisplayName("Method getCustomer should return 400 when Id is invalid")
    @Test
    void getCustomer_WhenIdInvalid_ShouldReturnBadRequest() {
        // Prepare
        var customerId = "invalid_Id";

        // Act & Assert
        mockMvc.perform(get(REQUEST_URI + "/" + customerId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).getById(any(Long.class));
    }

    @SneakyThrows
    @DisplayName("Method getCustomers should return 200")
    @Test
    void getCustomers_ShouldReturnStatusOk() {
        // Prepare
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get(REQUEST_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(customers.getFirst().getId()))
                .andExpect(jsonPath("$.size()").value(customers.size()))
                .andDo(print());
    }

    @SneakyThrows
    @DisplayName("Method updateCustomer should return 200 when input data is valid")
    @Test
    void updateCustomer_WithValidData_ShouldReturnStatusIsOk() {
        // Prepare
        var customerId = 999L;
        var customer = customerBuilder()
                .id(customerId)
                .build();

        var customerUpdateDTO = mapper.toCustomerUpdateDTO(customer);
        when(customerService.update(eq(customerId), any(Customer.class))).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(put(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.fullName").value(customerUpdateDTO.getFullName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customerUpdateDTO.getPhone()));
    }

    @SneakyThrows
    @DisplayName("Method updateCustomer should return 400 when input data is invalid")
    @ParameterizedTest
    @MethodSource("invalidCustomerFieldsForUpdate")
    void updateCustomer_WithInValidData_ShouldReturnBadRequest(CustomerFiledName fieldName, String errorMessage) {
        // Prepare
        var customerId = 999L;
        var customer = customerBuilder()
                .withInvalid(fieldName)
                .build();
        var customerUpdateDTO = mapper.toCustomerUpdateDTO(customer);

        // Act & Assert
        mockMvc.perform(put(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).update(eq(customerId), any(Customer.class));
    }

    @SneakyThrows
    @DisplayName("Method updateCustomer should return 400 when request body is missing")
    @Test
    void updateCustomer_WithEmptyBody_ShouldReturnBadRequest() {
        // Prepare
        var customerId = 999L;
        var errorMessage = "Required request body is missing";

        // Act & Assert
        mockMvc.perform(put(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).update(eq(customerId), any(Customer.class));
    }

    public static Stream<Arguments> invalidCustomerFieldsForUpdate() {
        return Stream.of(
                Arguments.of(FULL_NAME, "must not be blank"),
                Arguments.of(PHONE, PHONE_FORMAT_MESSAGE)
        );
    }

    @SneakyThrows
    @DisplayName("Method updateCustomer should return 404 when customer is not present")
    @Test
    void updateCustomer_WhenCustomerIsNotPresent_ShouldReturnStatusNotFound() {
        // Prepare
        var customerId = 999L;
        var customer = customerBuilder().build();
        var customerUpdateDTO = mapper.toCustomerUpdateDTO(customer);
        var errorMessage = Customer.class.getSimpleName() + " with `Id: " + customerId + "` was not found!";

        when(customerService.update(eq(customerId), any(Customer.class)))
                .thenThrow(new EntityNotFoundException(Customer.class.getSimpleName(), "Id: " + customerId));

        // Act & Assert
        mockMvc.perform(put(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @SneakyThrows
    @DisplayName("Method updateCustomer should return 400 when Id is not valid")
    @Test
    void updateCustomer_WhenIdIsNotValid_ShouldReturnBadRequest() {
        // Prepare
        var customerId = "ID";
        var customer = customerBuilder().build();
        var customerUpdateDTO = mapper.toCustomerUpdateDTO(customer);

        // Act & Assert
        mockMvc.perform(put(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).update(anyLong(), any(Customer.class));
    }

    @SneakyThrows
    @DisplayName("Method deleteCustomer should return 204")
    @Test
    void deleteCustomer_ShouldReturnStatusNoContent() {
        // Prepare
        var customerId = 999L;

        // Act & Assert
        mockMvc.perform(delete(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify
        verify(customerService).delete(anyLong());
    }

    @SneakyThrows
    @DisplayName("Method deleteCustomer should return 404 when customer is no present")
    @Test
    void deleteCustomer_WhenCustomerNoPresent_ShouldReturnStatusNotFound() {
        // Prepare
        var customerId = 999L;

        doThrow(new EntityNotFoundException(Customer.class.getSimpleName(), "Id: " + customerId))
                .when(customerService).delete(customerId);

        // Act & Assert
        mockMvc.perform(delete(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @SneakyThrows
    @DisplayName("Method deleteCustomer should return 400 when Id is not valid")
    @Test
    void deleteCustomer_WhenIdIsNotValid_ShouldReturnBadRequest() {
        // Prepare
        var customerId = "ID";

        // Act & Assert
        mockMvc.perform(delete(REQUEST_URI + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // Verify
        verify(customerService, never()).delete(anyLong());
    }
}