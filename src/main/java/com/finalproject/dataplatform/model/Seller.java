package com.finalproject.dataplatform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sellers")
@Data
public class Seller {

    @Id
    @Column(name = "seller_id", length = 64)
    private String sellerId;

    @Column(name = "seller_zip_code_prefix")
    private Integer sellerZipCodePrefix;

    @Column(name = "seller_city", length = 128)
    private String sellerCity;

    @Column(name = "seller_state", length = 16)
    private String sellerState;
}