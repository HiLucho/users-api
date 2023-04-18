package com.luis.project.cl.users.infrastructure.adapter.out.persistence.mapper;


import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * User Mapper.
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

  public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "isActive", source = "active")
  public abstract User dtoToModel(UserDto userDto);

  @Mapping(target = "isActive", source = "active")
  public abstract UserDto modelToDto(User user);

}
