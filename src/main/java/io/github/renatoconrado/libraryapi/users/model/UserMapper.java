package io.github.renatoconrado.libraryapi.users.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    Users toEntity(UserDTO userDTO);

    UserDTO userToDTO(Users user);

    @Mapping(source = "login", target = "username")
    UserSafeDTO userToSafeDTO(Users users);
}
