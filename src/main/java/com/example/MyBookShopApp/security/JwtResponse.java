package com.example.MyBookShopApp.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {

    private String message;
    private Status status;
    private String exceptionType;
    private String jwt;
    private Jws<Claims> jws;

    public enum Status {
        SUCCESS, ERROR
    }

    public JwtResponse() {
    }

    public JwtResponse(String jwt) {
        this.jwt = jwt;
        log.debug("jwt: " + jwt);
        this.status = Status.SUCCESS;
    }

    public JwtResponse(Jws<Claims> jws) {
        log.debug("jws claims: " + jws.getBody());
        this.jws = jws;
        this.status = Status.SUCCESS;
    }

}
