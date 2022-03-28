package com.example.userservice.dto;


import com.example.userservice.domain.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createdAt;
    private String encrytedPwd;

    public User toEntity(){
        return User.builder()
                .email(email)
                .name(name)
                .userId(userId)
                .encrytedPwd(encrytedPwd)
                .build();
    }
    @Builder
    public UserDto(User entity){
        this.name = entity.getName();
        this.userId = entity.getUserId();
        this.email = entity.getEmail();
        this.encrytedPwd = entity.getEncrytedPwd();
    }
}
