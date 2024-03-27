package org.example.api.users.filter;


import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.api.users.service.JwtService;
import org.example.api.users.service.UserDetailsImp;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsImp userDetailsImp;

    public JwtFilter(JwtService jwtService, UserDetailsImp userDetailsImp) {
        this.jwtService = jwtService;
        this.userDetailsImp = userDetailsImp;
    }

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull FilterChain filterChain) throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");
            if ((authorizationHeader == null) || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

           if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails = userDetailsImp.loadUserByUsername(username);

                if(jwtService.validateToken(token, userDetails)) {
                     UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                     usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                     SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

           }
           filterChain.doFilter(request, response);

        }
    }
