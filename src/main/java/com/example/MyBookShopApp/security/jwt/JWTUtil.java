package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.entity.other.TokenEntity;
import com.example.MyBookShopApp.exception.JwtTokenMalformedException;
import com.example.MyBookShopApp.exception.JwtTokenMissingException;
import com.example.MyBookShopApp.repository.TokenRepository;
import com.example.MyBookShopApp.security.UserEntityDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${auth.secret}")
    private String secret;

    @Value("${jwtExpirationInMs}")
    private int jwtExpirationMs;

    private TokenRepository tokenRepository;

    @Autowired
    public JWTUtil(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }

    public String generateToken(UserEntityDetails userEntityDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userEntityDetails.getUsername());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Boolean isTokenInBlackList(String token) {
        TokenEntity entity = tokenRepository.findByToken(token);
        return entity == null;
    }

    public Boolean validateToken(String token, UserEntityDetails userEntityDetails) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
        String username = extractUsername(token);
        return (username.equals(userEntityDetails.getUsername()) && !isTokenExpired(token) && isTokenInBlackList(token));
    }
}
