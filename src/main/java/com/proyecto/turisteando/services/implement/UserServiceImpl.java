package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.entities.UserEntity;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IUserMapper userMapper;

    @Override
    public Iterable<UserResponseDto> getAll() {
        return userMapper.toDtoList(userRepository.findAll());
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
        return null;
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            userMapper.partialUpdate(userRequestDto, userEntity);
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
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
        UserEntity  userEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            return userMapper.toDto(userEntity);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
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
