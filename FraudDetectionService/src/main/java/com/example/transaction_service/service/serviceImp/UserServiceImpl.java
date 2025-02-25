package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entitie.Bank;
import com.example.transaction_service.entitie.User;
import com.example.transaction_service.mapper.IUserMapper;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
            System.out.println("User Not found");
            return null;
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
            throw new RuntimeException("not found");
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
}
