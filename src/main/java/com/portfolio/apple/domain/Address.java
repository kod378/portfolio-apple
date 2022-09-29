package com.portfolio.apple.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;
    private String street;
    private int zipcode;
}
