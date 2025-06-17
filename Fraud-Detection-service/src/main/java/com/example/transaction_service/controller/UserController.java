package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("userss")

public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id)
    {

        userService.deleteUser(id);
    }

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

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto userDto, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        userDto.setUserId(currentUser.getUserId());
        UserDto updatedUser = userService.updateUser(userDto, currentUser.getUserId());
        return ResponseEntity.ok(updatedUser);
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
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            // Vérification type MIME
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Seuls les fichiers images sont autorisés.");
            }

            Path uploadPath = Paths.get("C:/Users/Lenovo/Desktop/stagePfe/BackOffice_PFE/pfaMicros/Fraud-Detection-service/images").toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Supprimer ancienne image (optionnel)
                if (user.getProfileImageUrl() != null) {
                    Path oldImage = uploadPath.resolve(Paths.get(user.getProfileImageUrl()).getFileName());
                    Files.deleteIfExists(oldImage);
                }

                user.setProfileImageUrl("/images/" + fileName);
                userRepository.save(user);
            }

            return ResponseEntity.ok("/images/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur d'upload");
        }
    }



}
