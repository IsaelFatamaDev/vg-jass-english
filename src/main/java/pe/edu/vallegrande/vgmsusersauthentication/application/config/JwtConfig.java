package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.exception.CustomException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtConfig {

     @Value("${jwt.secret}")
     private String jwtSecret;

     @Value("${jwt.expiration}")
     private Long jwtExpiration;

     @Value("${jwt.refresh-expiration}")
     private Long jwtRefreshExpiration;

     @Value("${jwt.issuer}")
     private String jwtIssuer;

     private SecretKey getSigningKey() {
          return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
     }

     public String generateAccessToken(String userId, String username, List<RolesUsers> roles) {
          Date now = new Date();
          Date expiryDate = new Date(now.getTime() + jwtExpiration);

          return Jwts.builder()
                    .subject(userId)
                    .claim("username", username)
                    .claim("roles", roles)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .issuer(jwtIssuer)
                    .signWith(getSigningKey())
                    .compact();
     }

     public String generateRefreshToken(String userId) {
          Date now = new Date();
          Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

          return Jwts.builder()
                    .subject(userId)
                    .claim("type", "refresh")
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .issuer(jwtIssuer)
                    .signWith(getSigningKey())
                    .compact();
     }

     public Claims extractClaims(String token) {
          try {
               return Jwts.parser()
                         .verifyWith(getSigningKey())
                         .build()
                         .parseSignedClaims(token)
                         .getPayload();
          } catch (Exception e) {
               log.error("Error extrayendo claims del token: {}", e.getMessage());
               throw new CustomException("Token inválido", "INVALID_TOKEN", 401);
          }
     }

     public String extractUserId(String token) {
          return extractClaims(token).getSubject();
     }

     public String extractUsername(String token) {
          return extractClaims(token).get("username", String.class);
     }

     @SuppressWarnings("unchecked")
     public List<String> extractRoles(String token) {
          Claims claims = extractClaims(token);
          return claims.get("roles", List.class);
     }

     public boolean isTokenValid(String token) {
          try {
               Claims claims = extractClaims(token);
               return !isTokenExpired(claims);
          } catch (Exception e) {
               return false;
          }
     }

     public boolean isRefreshTokenValid(String token) {
          try {
               Claims claims = extractClaims(token);
               String tokenType = claims.get("type", String.class);
               return "refresh".equals(tokenType) && !isTokenExpired(claims);
          } catch (Exception e) {
               return false;
          }
     }

     private boolean isTokenExpired(Claims claims) {
          return claims.getExpiration().before(new Date());
     }

     public Long getJwtExpiration() {
          return jwtExpiration;
     }
}
