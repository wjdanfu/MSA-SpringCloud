package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResDto;
import com.example.userservice.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    private Environment environment;
    private UserService userService;

    @Autowired
    public UserController(Environment environment, UserService userService) {
        this.userService  = userService;
        this.environment = environment;
    }

    @GetMapping("/health-check")
    public String status(){

        return String.format(
                "good good vert good %s",environment.getProperty("local.server.port")
        + ", port(server.port)=" + environment.getProperty("server.port")
        + " ,token secret = " + environment.getProperty("token.secret"));
    }

    @GetMapping("/welcome")
    public String welcome(){
        return environment.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserDto userDto){
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResDto>> findAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUser());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResDto> findUserById(@PathVariable String userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(userId));
    }
}
