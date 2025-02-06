package com.khokhlov.weather.filter;

import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/auth/register");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (EXCLUDED_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String uuid = request.getRequestedSessionId();
        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session ID is null");
        }


        UUID sessionId;
        try {
            sessionId = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid Session ID");
            return;
        }

        User user = null;

        Optional<Session> sessionOpt = sessionService.findSessionById(sessionId);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                user = session.getUser();
                request.setAttribute("user", user);
            }
        }

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session is invalid or expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
