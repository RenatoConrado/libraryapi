package io.github.renatoconrado.libraryapi.users.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO userToDTO(User user);

    @Mapping(source = "login", target = "username")
    UserSafeDTO userToSafeDTO(User user);
}
