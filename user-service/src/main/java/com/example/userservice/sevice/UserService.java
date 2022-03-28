package com.example.userservice.sevice;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
    void createUser(UserDto userDto);
    List<UserResDto> findAllUser();
    UserResDto findUserById(String userId);

    UserDto getUserDetailsByEmail(String email);
}
