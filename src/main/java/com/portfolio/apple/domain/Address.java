package com.portfolio.apple.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address{

    private String address;
    private String detailAddress;
    private Integer postcode;
    private String phoneNumber;
    private String addressee;
}
