package com.tulip.project.configuration;

import com.tulip.project.jwt.JWTTokenService;
import com.tulip.project.repository.BlacklistedTokenDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JWTTokenService jwtTokenService;
    private final BlacklistedTokenDAO blacklistedTokenDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            if (blacklistedTokenDAO.isTokenBlacklisted(jwt)) {
                response.sendError(401);
                throw new ServletException("token expire");
            }
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                List<String> authorities = jwtTokenService.extractAuthorities(jwt);
                List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();

                if(jwtTokenService.validateToken(jwt)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(null, null, simpleGrantedAuthorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
