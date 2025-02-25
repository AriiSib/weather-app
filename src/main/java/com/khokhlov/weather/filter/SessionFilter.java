package com.khokhlov.weather.filter;

import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

        Cookie[] cookies = request.getCookies();
        String sessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_ID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        if (sessionId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }


        UUID uuid;
        try {
            uuid = UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }


        Optional<Session> session = sessionService.findSessionById(uuid);
        if (session.isEmpty() || session.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = session.get().getUser();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("user", user);

        filterChain.doFilter(request, response);
    }
}
