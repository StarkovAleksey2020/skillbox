package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class UserEntityRegister {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserEntityRegister(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }



    public UserEntity registerNewUser(RegistrationForm registrationForm) {

        UserEntity userEntity = new UserEntity();
        if (userEntityRepository.findUserEntityByEmail(registrationForm.getEmail()) == null) {
            userEntity.setName(registrationForm.getName());
            userEntity.setEmail(registrationForm.getEmail());
            userEntity.setPhone(registrationForm.getPhone());
            userEntity.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            userEntity.setBalance(0);
            userEntity.setRegTime(LocalDateTime.now());
            userEntity.setHash(UUID.randomUUID().toString());
            userEntityRepository.save(userEntity);
        }
        return userEntity;
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(HttpServletRequest httpServletRequest, ContactConfirmationPayload payload) {

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode());
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        UserEntityDetails userEntityDetails = (UserEntityDetails) userDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userEntityDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Object getCurrentUser() {

        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (o instanceof DefaultOAuth2User) {
            UserEntity userEntity = new UserEntity();

            return userEntity;
        } else {
            UserEntityDetails userEntityDetails =
                    (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userEntityDetails.getUserEntity();
        }
    }

    public Object getPrincipal(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
