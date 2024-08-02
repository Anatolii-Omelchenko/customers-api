package tech.theraven.customers_api.model.dto;

import lombok.Data;

@Data
public class CustomerInfo {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
}
