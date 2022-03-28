package com.example.userservice.dto;


import com.example.userservice.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResDto {
    private String name;
    private String id;
    private String email;
    private List<OrderResDto> orderResDtos;

    @Builder
    public UserResDto(User entity){
        this.name = entity.getName();
        this.id = entity.getUserId();
        this.email = entity.getEmail();
    }
}
