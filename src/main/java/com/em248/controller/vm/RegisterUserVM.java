package com.em248.controller.vm;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterUserVM {


    @NotNull@NotEmpty
    private String email;

    @NotNull@NotEmpty
    private String password;
}
