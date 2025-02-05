package com.khokhlov.weather.filter;

import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Order(1)
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    private  SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

//        String sessionId = request.getRequestedSessionId();
        String uuid = UUID.randomUUID().toString();
        UUID sessionId = UUID.fromString(uuid);


        if (sessionId != null) {
            Session session = sessionService.findSessionById(sessionId);
            if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                User user = session.getUser();
                request.setAttribute("user", user);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new RuntimeException("Session id is null");
        }
        filterChain.doFilter(request, response);
    }
}
