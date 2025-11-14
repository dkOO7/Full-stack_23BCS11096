package com.tradingapp.trading_platform.auth; // Use your correct package name

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JpaUserDetailsService jpaUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

   @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    logger.info("--- JWT Filter is processing request to: {} ---", request.getRequestURI());

    try {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt)) {
            logger.info("Found JWT in request header.");
            if (jwtTokenProvider.validateToken(jwt)) {
                logger.info("JWT is valid.");
                String username = jwtTokenProvider.getUsernameFromJWT(jwt);
                logger.info("Username from JWT: {}", username);

                UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User '{}' successfully authenticated and set in security context.", username);
            } else {
                logger.warn("JWT validation failed.");
            }
        } else {
            logger.info("No JWT found in Authorization header.");
        }
    } catch (Exception ex) {
        logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
}


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}