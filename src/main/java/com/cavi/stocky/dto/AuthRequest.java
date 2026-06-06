package com.cavi.stocky.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
