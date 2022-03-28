package com.example.userservice.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String username);
    User findByUserId(String userId);

}
