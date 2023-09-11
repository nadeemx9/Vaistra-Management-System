package com.vaistra.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver exceptionResolver;


    public JwtAuthenticationFilter(HandlerExceptionResolver exceptionResolver, JwtService jwtService, UserDetailsService userDetailsService)
    {
        this.exceptionResolver = exceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                //looking good
                token = requestHeader.substring(7);

                username = this.jwtService.getUsernameFromToken(token);

            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //fetch user detail from username
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                Boolean validateToken = this.jwtService.validateToken(token, userDetails);
                if (validateToken) {

                    //set the authentication
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
