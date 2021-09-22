package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.other.TokenEntity;
import com.example.MyBookShopApp.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private TokenRepository tokenRepository;

    @Autowired
    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        String token = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            TokenEntity tokenEntity = tokenRepository.findByToken(token);
            if (tokenEntity == null) {
                TokenEntity entity = new TokenEntity();
                entity.setToken(token);
                tokenRepository.save(entity);
            }
        }
    }
}
