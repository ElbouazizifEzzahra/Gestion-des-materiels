package com.rouandi.rouandi.jwt;

import com.rouandi.rouandi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
@Service
public class JwtUtil {
  public static final String SECRET = ${key_secret};


public String generateToken(User user) {
  String token =Jwts
          .builder()
          .setSubject(user.getEmail())
          .claim("id",user.getId())
          .claim("role",user.getRole())
          .claim("name", user.getNom())
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() +400*60*1000))
          .signWith(getSignKey())
            .compact();
    return token;
}


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        JwtParserBuilder builder = Jwts.parserBuilder().setSigningKey(getSignKey());
        builder.setAllowedClockSkewSeconds(60);
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }


   public  Boolean validiteToken(String token , UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }
 

}
