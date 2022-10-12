package com.portfolio.apple.domain.account.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class AdminLoginFormDTO {

    @NonNull
    @Length(min = 3)
    private String accountId;

    @NotEmpty
    private String password;
}