package com.portfolio.apple.domain.account.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class AdminJoinFormDTO {

    @NonNull
    @Length(min = 3)
    private String accountId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirm;

    public AdminJoinFormDTO(String accountId, String password, String passwordConfirm) {
        this.accountId = accountId;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
