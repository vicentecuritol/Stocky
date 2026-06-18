package com.cavi.stocky.dto;


import lombok.Data;
import lombok.ToString;

@Data
public class AuthRequest {
    private String username;

    @ToString.Exclude
    private String password;
}
