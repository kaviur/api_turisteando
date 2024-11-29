package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.RoleRequestDto;
import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.entities.enums.Role;
import com.proyecto.turisteando.mappers.IUserMapper;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.services.IUserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IUserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Iterable<UserResponseDto> getAll() {
        return userMapper.toDtoList(userRepository.findByIsActiveTrue());
    }

    @Override
    public UserResponseDto read(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        try {
            Role role = Optional.ofNullable(userRequestDto.getRole())
                    .orElse(Role.BUYER);
            UserEntity userEntity = userMapper.toEntity(userRequestDto);
            userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            userEntity.setRole(role);
            return userMapper.toDto(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new ServiceException("Error al crear el usuario"+ e.getMessage());
        }
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            userMapper.partialUpdate(userRequestDto, userEntity, passwordEncoder);
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            userEntity.setRole(roleRequestDto.getRole());
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el rol del usuario" + e.getMessage());
        }
    }

    @Override
    public UserResponseDto delete(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            userEntity.setIsActive(false);
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserResponseDto toggleStatus(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            userEntity.setIsActive(!userEntity.getIsActive());
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Iterable<UserResponseDto> getAllByFilters(UserRequestDto dto) {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.toDtoList(filterUsers(userEntities, dto));
    }

    @Override
    public UserResponseDto getCurrentUser(Authentication authentication) {
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserResponseDto toggleUserRole(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            // Cambiar el rol entre ADMIN y BUYER
            if (userEntity.getRole() == Role.BUYER) {
                userEntity.setRole(Role.ADMIN);
            } else if (userEntity.getRole() == Role.ADMIN) {
                userEntity.setRole(Role.BUYER);
            }
            // Guardar el cambio en la base de datos
            userRepository.save(userEntity);

            // Devolver el DTO actualizado
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException("Error al cambiar el rol del usuario: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(
                userDetails.getUsername(),
                "",
                userDetails.getAuthorities()
        );

    }

    List<UserEntity> filterUsers(List<UserEntity> users, UserRequestDto dto) {
        return users.stream()
                .filter(user -> dto.getName() == null ||
                        user.getName().toLowerCase().contains(dto.getName().toLowerCase()))
                .filter(user -> dto.getLastName() == null ||
                        user.getLastName().toLowerCase().contains(dto.getLastName().toLowerCase()))
                .filter(user -> dto.getEmail() == null ||
                        user.getEmail().toLowerCase().contains(dto.getEmail().toLowerCase()))
                .toList();
    }
}
