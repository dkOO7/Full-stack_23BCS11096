package com.tradingapp.trading_platform.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // This should be a strong, secret key and ideally stored securely, not hardcoded.
    // It must be long enough for the algorithm (e.g., 256 bits for HS256).
    private final String jwtSecret = "aVeryLongAndSecureSecretKeyThatIsAtLeast256BitsLongForHS512";

    // Token validity in milliseconds (1 hour)
    private final long jwtExpirationInMs = 3600000;

    // Generate Key object from secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                // The modern .signWith() only takes the key.
                // The algorithm is inferred from the key itself.
                .signWith(getSigningKey())
                .compact();
    }

    // Extract username from JWT
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (io.jsonwebtoken.UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
