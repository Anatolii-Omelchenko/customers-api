package tech.theraven.customers_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.theraven.customers_api.model.Customer;
import tech.theraven.customers_api.model.dto.CustomerInfo;
import tech.theraven.customers_api.model.dto.CustomerCreateDTO;
import tech.theraven.customers_api.model.dto.CustomerUpdateDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Customer toCustomer(CustomerCreateDTO customerCreateDTO);

    CustomerInfo toCustomerInfo(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Customer toCustomer(CustomerUpdateDTO customerUpdateDTO);

    CustomerCreateDTO toCustomerCreateDTO(Customer customer);

    CustomerUpdateDTO toCustomerUpdateDTO(Customer customer);
}
