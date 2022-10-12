package com.portfolio.apple.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address{

    private String city;
    private String street;
    private Integer zipcode;
}
