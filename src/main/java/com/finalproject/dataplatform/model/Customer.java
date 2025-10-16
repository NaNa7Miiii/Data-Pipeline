package com.finalproject.dataplatform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @Column(name = "customer_id", length = 64)
    private String customerId;

    @Column(name = "customer_unique_id", length = 64)
    private String customerUniqueId;

    @Column(name = "customer_zip_code_prefix")
    private Integer customerZipCodePrefix;

    @Column(name = "customer_city", length = 128)
    private String customerCity;

    @Column(name = "customer_state", length = 16)
    private String customerState;
}

