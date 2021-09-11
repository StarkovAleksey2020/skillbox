package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserEntityRegister {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserEntityRegister(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void registerNewUser(RegistrationForm registrationForm) {

        if (userEntityRepository.findUserEntityByEmail(registrationForm.getEmail()) == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(registrationForm.getName());
            userEntity.setEmail(registrationForm.getEmail());
            userEntity.setPhone(registrationForm.getPhone());
            userEntity.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            userEntity.setBalance(0);
            userEntity.setRegTime(LocalDateTime.now());
            userEntity.setHash(UUID.randomUUID().toString());
            userEntityRepository.save(userEntity);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(true);
        return response;
    }

    public Object getCurrentUser() {
        UserEntityDetails userEntityDetails =
                (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userEntityDetails.getUserEntity();
    }
}
