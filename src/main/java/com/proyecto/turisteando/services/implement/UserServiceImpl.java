package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.services.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements ICrudService<UserRequestDto, Long>, UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public Iterable<UserRequestDto> getAll() {
        return null;
    }

    @Override
    public UserRequestDto read(Long id) {
        return null;
    }

    @Override
    public UserRequestDto create(UserRequestDto dto) {
        return null;
    }

    @Override
    public UserRequestDto update(UserRequestDto dto, Long id) {
        return null;
    }

    @Override
    public UserRequestDto delete(Long id) {
        return null;
    }

    @Override
    public UserRequestDto toggleStatus(Long id) {
        return null;
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
}
