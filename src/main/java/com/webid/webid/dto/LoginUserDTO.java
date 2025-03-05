package com.webid.webid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {
    private String email;
    private String username;
    private String password;
}
