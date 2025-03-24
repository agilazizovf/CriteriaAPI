package com.api.criteria.service;

import com.api.criteria.entity.UserEntity;
import com.api.criteria.repository.UserCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserCriteriaRepository repository;

    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserEntity add(UserEntity user) {
        repository.save(user);
        return user;
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
