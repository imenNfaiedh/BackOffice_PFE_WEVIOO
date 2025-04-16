package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.enumeration.UserRole;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.IUserMapper;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserMapper userMapper;
    @Override
    public List<UserDto> getAllUser() {
        List<User> users= userRepository.findAll();
        return userMapper.toDto(users) ;
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent())
        {
            return userMapper.toDto(optionalUser.get());
        }
        else {
            throw new NotFoundException("USER with ID " + id + " not found");
        }

    }

    @Override
    public UserDto createUser(User user) {
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("USER with ID " + id + " not found");
        }

        User user = userOptional.get();
        user.setRole(userDto.getRole());
        user.setEmail(userDto.getEmail());
        user.setTel(userDto.getTel());
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        //user.getSuspicious_activity(userDto.getSuspicious_activity());



        return userMapper.toDto(userRepository.save(user));

    }

    @Override
    public void deleteUser(Long id) {

    }



    public User getCurrentUser(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();

        // Extraire l'ID Keycloak (sub)
        String keycloakId = jwt.getSubject(); // Le 'sub' du token Keycloak est l'ID de l'utilisateur
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        // Récupérer les rôles de l'utilisateur à partir du token
        List<String> roles = jwt.getClaim("realm_access") != null
                ? (List<String>) ((Map<String, Object>) jwt.getClaim("realm_access")).get("roles")
                : List.of();

        // Chercher l'utilisateur dans la base de données
        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Si l'utilisateur n'existe pas, on le crée
                    UserRole userRole = UserRole.CUSTOMER; // Valeur par défaut
                    if (roles.contains("ADMINISTRATEUR")) {
                        userRole = UserRole.ADMIN;
                    } else if (roles.contains("CHEF_AGENT")) {
                        userRole = UserRole.MANAGER;
                    }

                    // Créer un nouvel utilisateur
                    User user = new User();
                    user.setKeycloakId(keycloakId);
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRole(userRole);
                    user.setTel("");
                    user.setSuspicious_activity(false);
                    log.info("Saving user: {}", user);
                    // Sauvegarder dans la base de données
                    return userRepository.save(user);
                });
    }
}