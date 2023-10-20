package com.vinceadamo.dataapi.dataapi;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vinceadamo.dataapi.dataapi.services.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Claims claims = jwtTokenUtil.resolveClaims(request);
        if (claims == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!jwtTokenUtil.validateClaims(claims)) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println(claims);

        String username = jwtTokenUtil.getEmail(claims);

        System.out.println(username);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        System.out.println(userDetails);

        UsernamePasswordAuthenticationToken
        authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails == null ?
                List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(authentication);

        chain.doFilter(request, response);
    }

}
