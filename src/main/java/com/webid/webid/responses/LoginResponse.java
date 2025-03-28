package com.webid.webid.responses;

import java.util.List;
import java.util.Set;

import com.webid.webid.model.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private long ttl;
    private List<RoleEnum> roles;
}
