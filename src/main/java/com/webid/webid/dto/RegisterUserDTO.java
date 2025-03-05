package com.webid.webid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
