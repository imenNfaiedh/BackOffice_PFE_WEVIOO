package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.enumeration.UserRole;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.IBankAccountMapper;
import com.example.transaction_service.mapper.IUserMapper;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IUserService;
import lombok.AllArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private IBankAccountMapper bankAccountMapper;
    @Autowired
    private IBankAccountRepository bankAccountRepository;
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
        user.setCin(userDto.getCin());        // ajout
        user.setAddress(userDto.getAddress()); // ajout
        user.setUserName(userDto.getUserName());
        // user.setSuspicious_activity(userDto.getSuspicious_activity());

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }





    public User getCurrentUser(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();

        // Extraire l'ID Keycloak (sub)
        String keycloakId = jwt.getSubject(); // Le 'sub' du token Keycloak est l'ID de l'utilisateur
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String userName = jwt.getClaimAsString("preferred_username");
        String setSuspicious_activity = jwt.getClaimAsString("suspicious_activity");
        String tel = jwt.getClaimAsString("phone_number");

        // Récupérer les rôles de l'utilisateur à partir du token
        List<String> roles = jwt.getClaim("realm_access") != null
                ? (List<String>) ((Map<String, Object>) jwt.getClaim("realm_access")).get("roles")
                : List.of();

        // Chercher l'utilisateur dans la base de données
        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {

                    UserRole userRole = UserRole.CUSTOMER; // rôle par défaut

                    for (String role : roles) {
                        try {
                            log.info("Roles in token: {}", roles);
                            userRole = UserRole.valueOf(role);
                            break;
                        } catch (IllegalArgumentException ignored) {
                        }
                    }

                    // Créer un nouvel utilisateur
                    User user = new User();
                    user.setKeycloakId(keycloakId);
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRole(userRole);
                    user.setTel(tel);
                    user.setUserName(userName);
                    user.setSuspicious_activity(Boolean.valueOf(setSuspicious_activity));
                    log.info("Saving user: {}", user);
                    // Sauvegarder dans la base de données
                    return userRepository.save(user);
                });
    }



    public List<BankAccountDto> getBankAccountsByUser(Long userId) {
        List<BankAccount> accounts = bankAccountRepository.findByUser_UserId(userId);
        return bankAccountMapper.toDto(accounts);
//        return accounts.stream()
//                .map(account -> new BankAccountDto(
//                        account.getBankAccountId(),
//                        account.getAccountNumber(),
//                        account.getOpeningDate(),
//                        account.getBalance(),
//                        account.getFraudCount(),
//                        account.getIsBlocked(),
//                        account.getTypeBankAccount(),
//                        account.getUser().getFirstName(),
//                        account.getUser().getLastName() ))
//
//
//                .collect(Collectors.toList());
//    }

}}