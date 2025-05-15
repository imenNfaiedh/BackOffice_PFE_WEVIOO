package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {

    List<UserDto> getAllUser ();
    UserDto getUserById (Long id);
    UserDto createUser (User user);
    UserDto updateUser (UserDto userDto  , Long id);
    void deleteUser (Long id);
    public User getCurrentUser(Authentication authentication);
    List<BankAccountDto> getBankAccountsByUser(Long userId);
}
