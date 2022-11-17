package com.portfolio.apple.domain.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersRequestDTO {

    @NotEmpty
    private String addressee;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String address;
    @NotEmpty
    private String detailAddress;
    @NotNull
    private Integer postcode;

    private List<Long> checkShoppingItemIds;
}
