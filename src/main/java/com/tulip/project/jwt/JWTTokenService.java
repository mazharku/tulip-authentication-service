package com.tulip.project.jwt;

import com.tulip.project.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTTokenService {
    @Value("${jwt-secret}")
    private String secret;

    @Value("${jwt.token-expire-time}")
    private int tokenExpireTime;

    @Value("${jwt.refresh.token-expire-time}")
    private int refreshTokenExpireTime;

    public String generateToken(User user) {
        Date expire = new Date(new Date().getTime() + tokenExpireTime * 60 * 1000);
        List<String> authorities = user.getAuthorities().stream()
                .map(e-> e.getName().name()).toList();
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String loginUserId(String jwt) {
        return parseClaimsFromJWT(jwt).get("id").toString();
    }

    public String extractUsername(String jwt) {
        if (jwt == null) return null;
        return parseClaimsFromJWT(jwt).get("name").toString();
    }

    public String extractEmail(String jwt) {
        if (jwt == null) return null;
        return parseClaimsFromJWT(jwt).get("email").toString();
    }

    public List<String> extractAuthorities(String jwt) {
        if (jwt == null) return new ArrayList<>();
        return (List<String>) parseClaimsFromJWT(jwt).get("authorities");
    }

    private Claims parseClaimsFromJWT(String jwt) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt).getBody();
    }

    public void parseClaims(String jwt) {
         Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt).getBody();
    }

    public String generateRefreshToken(){
        long refreshTokenExpiryMillis = (long) refreshTokenExpireTime *60*1000;
        Date validity = new Date(new Date().getTime() + refreshTokenExpiryMillis);
        return  Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Date getExpiryFromJwt(String jwt) {
        return parseClaimsFromJWT(jwt).getExpiration();
    }

}
