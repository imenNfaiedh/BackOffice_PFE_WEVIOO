package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("userss")

public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        // Appel de la méthode getCurrentUser pour obtenir l'utilisateur authentifié
        User user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    public List<UserDto> getAllUser()
    {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getByIdUser (@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("")
    public UserDto createUser(@RequestBody User user)
    {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public  UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto)
    {
        userDto.setUserId(id);
        return userService.updateUser(userDto,id);
    }
    @GetMapping("/count")
    public long countUser() {
        return userRepository.count();
    }


    // Endpoint GET pour récupérer la liste des comptes bancaires d’un user
    @GetMapping("/{userId}/bank-accounts")
    public ResponseEntity<List<BankAccountDto>> getBankAccountsByUser(@PathVariable Long userId) {
        List<BankAccountDto> accounts = userService.getBankAccountsByUser(userId);
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

}
