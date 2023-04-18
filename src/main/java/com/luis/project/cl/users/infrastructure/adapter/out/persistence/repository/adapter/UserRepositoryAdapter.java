package com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.adapter;

import com.luis.project.cl.users.application.ports.UserPersistencePort;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * User Repository Service.
 */
@Service
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserPersistencePort {

  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(UUID id) {
    return userRepository.findById(id).map(UserMapper.INSTANCE::dtoToModel);
  }

  @Override
  public Optional<User> findByCorreo(String correo) {
    return userRepository.findByEmail(correo).map(UserMapper.INSTANCE::dtoToModel);
  }

  @Override
  public Optional<User> save(User user) {
    var userDto = UserMapper.INSTANCE.modelToDto(user);
    final LocalDateTime now = LocalDateTime.now();
    userDto.setActive(true);
    userDto.setCreated(now);
    userDto.setLastLogin(now);
    userDto.setToken(UUID.randomUUID());
    var saved = userRepository.saveAndFlush(userDto);
    return Optional.of(userRepository.save(saved)).map(UserMapper.INSTANCE::dtoToModel);
  }

  @Override
  public Optional<User> update(User user) {
    final LocalDateTime now = LocalDateTime.now();
    var itemDto = UserMapper.INSTANCE.modelToDto(user);
    itemDto.setModified(now);
    return Optional.of(userRepository.save(itemDto)).map(UserMapper.INSTANCE::dtoToModel);
  }
}
