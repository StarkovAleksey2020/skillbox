package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityDetailService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserEntityDetailService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findUserEntityByEmail(s);
        if (userEntity != null) {
            return new UserEntityDetails(userEntity);
        } else {
            throw new UsernameNotFoundException("user not found!");
        }
    }
}
