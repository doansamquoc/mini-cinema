package com.sam.minicinemaapi.service.impl;

import com.sam.minicinemaapi.constant.ErrorCode;
import com.sam.minicinemaapi.entity.User;
import com.sam.minicinemaapi.exception.BusinessException;
import com.sam.minicinemaapi.mapper.UserMapper;
import com.sam.minicinemaapi.repostiory.UserRepository;
import com.sam.minicinemaapi.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserMapper mapper;
    UserRepository repository;

    @Override
    public User findByIdentifier(String identifier) {
        return repository.findByIdentifier(identifier)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    public User getReference(Long userId) {
        return repository.getReferenceById(userId);
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
