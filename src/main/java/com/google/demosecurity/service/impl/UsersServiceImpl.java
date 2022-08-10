package com.google.demosecurity.service.impl;

import com.google.demosecurity.entity.Users;
import com.google.demosecurity.repository.UsersRepository;
import com.google.demosecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public List<Users> getList() {
        return usersRepository.findAll();
    }
}
