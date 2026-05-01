package com.apiGateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;


/**
 * Shared JWT parsing helper used by the gateway before routing requests.
 */
@Component
public class JwtUtil {
    private final String SECRET_KEY = "ASHHDFHSOIUEUBDIFBUIEWGFVSDVFIWWEE487536DGKFHGHDSGFHKSDGFUEFUEVCUKEUFUDVCVDHSVHSDVHF";

    private Key getkey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public void validateToken(String token){
        // Parsing the claims throws when the token is expired, malformed, or signed with another key.
        Jwts.parserBuilder()
                .setSigningKey(getkey())
                .build()
                .parseClaimsJws(token);
    }

}
