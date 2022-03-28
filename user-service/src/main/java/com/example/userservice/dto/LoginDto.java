package com.example.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotNull(message = "이메일을 입력해 주세요")
    @Email
    private String email;
    @NotNull(message = "패시워드를 입력해 주셔요")
    @Size(min = 8, message = "패스와드는 최소 8자요")
    private String password;
}
