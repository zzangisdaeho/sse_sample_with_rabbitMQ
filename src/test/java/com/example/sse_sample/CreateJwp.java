package com.example.sse_sample;

import com.example.sse_sample.config.security.auth.jwt.AccessToken;
import com.example.sse_sample.document.Notification;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;

import static com.example.sse_sample.config.security.auth.jwt.JwtToken.GENERAL_TOKEN_KEY;

public class CreateJwp {

    private String jwt;

    @Test
    @BeforeEach
    public void generateJWT(){
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("userId", "123");


        String jwt = Jwts.builder()
                .setClaims(payload)
                .signWith(Keys.hmacShaKeyFor(GENERAL_TOKEN_KEY.getBytes()))
//                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
//                .signWith(SignatureAlgorithm.HS256, GENERAL_TOKEN_KEY.getBytes())
                .compact();

        System.out.println(jwt);
        System.out.println(GENERAL_TOKEN_KEY.getBytes());

        this.jwt = jwt;

    }

    @Test
    public void parsingJWT(){
        AccessToken accessToken = AccessToken.ofNullable(jwt);
        System.out.println("accessToken = " + accessToken);
    }

    @Test
    public void wtf(){
        String name = Notification.class.getName();
        System.out.println("name = " + name);
    }

}
