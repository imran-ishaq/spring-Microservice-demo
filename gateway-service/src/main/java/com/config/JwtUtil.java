package com.config;

import com.exceptions.TokenExceptions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token)  {
        boolean isTokenExp=false;
        boolean isTokenExpOccur=false;
        try {
            isTokenExp = this.getAllClaimsFromToken(token).getExpiration().before(new Date());
        }catch (Exception ex){
         isTokenExpOccur=true;

        }
        if(isTokenExpOccur)
        throw new TokenExceptions(401,"Token is not valid");

        return isTokenExp;
    }

    public boolean isInvalid(String token)  {
        return this.isTokenExpired(token);
    }

}
