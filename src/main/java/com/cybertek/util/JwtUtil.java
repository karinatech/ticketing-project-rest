package com.cybertek.util;

import com.cybertek.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String secret="cyber";

    public String generateToken(User user,String username){
        Map<String, Object>claims=new HashMap<>();
        claims.put("username",user.getUserName());
        claims.put("id",user.getId());
        claims.put("firstname",user.getFirstName());
        claims.put("lastname",user.getLastName());
        return createToken(claims,username);

    }
    public String createToken(Map<String,Object>claims,String username){
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*60*10))
                .signWith(SignatureAlgorithm.ES256,secret).compact();
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    }
    private <T>T extractClaim(String token, Function<Claims,T>claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);

    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String currentUser=extractAllClaims(token).get("id").toString();
        return (currentUser.equals(userDetails.getUsername())&& !isTokenExpired(token));
        }
        private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
        }

}
