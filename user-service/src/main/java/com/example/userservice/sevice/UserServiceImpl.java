package com.example.userservice.sevice;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.User;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.OrderResDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResDto;
import com.google.common.collect.Lists;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment environment;
    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null){
            throw new UsernameNotFoundException(username);

        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncrytedPwd(),
                true,true,true,true,
                new ArrayList<>());
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder
    ,Environment environment , RestTemplate restTemplate,OrderServiceClient orderServiceClient){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.environment=environment;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;

    }


    @Override
    public void createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncrytedPwd(passwordEncoder.encode(userDto.getPassword()));
        System.out.println(userDto.getEncrytedPwd());

        userRepository.save(userDto.toEntity());
    }

    @Override
    public List<UserResDto> findAllUser() {
        Iterable<User> entity = userRepository.findAll();
        List<UserResDto> userResDtos = new ArrayList<>();
        List<User> myList = Lists.newArrayList(entity);
        for (int i = 0; i<myList.size(); i++){
            UserResDto postResDto = new UserResDto(myList.get(i));
            userResDtos.add(postResDto);
        }
        return userResDtos;
    }

    @Override
    public UserResDto findUserById(String userId) {
        User entity = userRepository.findByUserId(userId);
        if (entity == null){
            throw new UsernameNotFoundException("User not fount");
        }
//        List<OrderResDto> orders = new ArrayList<>();

//        String orderUrl = String.format(environment.getProperty("order-service.url"),userId);
//        ResponseEntity<List<OrderResDto>> orderListResponse= restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<OrderResDto>>() {
//                });

        List<OrderResDto> ordersList = orderServiceClient.getOrders(userId);
//        try {
//            ordersList = orderServiceClient.getOrders(userId);
//        }catch (FeignException exception){
//            log.error(exception.getMessage());
//
//        }
        UserResDto userResDto = new UserResDto(entity);
//        List<OrderResDto> ordersList = orderListResponse.getBody();
        userResDto.setOrderResDtos(ordersList);
        return userResDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        User entity = userRepository.findByEmail(email);
        UserDto userDto = new UserDto(entity);
        if (entity == null){
            throw new UsernameNotFoundException(email);
        }
        return userDto;
    }
}
