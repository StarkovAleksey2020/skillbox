package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.exception.AuthenticationCredentialsNotFoundException;
import com.example.MyBookShopApp.exception.BadRequestException;
import com.example.MyBookShopApp.security.UserEntityDetailService;
import com.example.MyBookShopApp.security.UserEntityDetails;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final UserEntityDetailService userEntityDetailService;
    private final JWTUtil jwtUtil;

    @Autowired
    public JWTRequestFilter(UserEntityDetailService userEntityDetailService, JWTUtil jwtUtil) {
        this.userEntityDetailService = userEntityDetailService;
        this.jwtUtil = jwtUtil;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    username = jwtUtil.extractUsername(token);
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserEntityDetails userEntityDetails = (UserEntityDetails) userEntityDetailService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, userEntityDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userEntityDetails, null, userEntityDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        throw new BadCredentialsException("Invalid credentials");
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

