package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entitie.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})

public interface IUserMapper {
    UserDto toDto (User user);
    List<UserDto> toDto (List<User> users);
    User toEntity (UserDto userDto);
    List<User> toEntity (List<UserDto> userDtos);


}
