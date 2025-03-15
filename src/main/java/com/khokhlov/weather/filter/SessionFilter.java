package com.khokhlov.weather.filter;

import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import io.micrometer.common.lang.NonNullApi;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@Component
@NonNullApi
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "SESSION_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

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

        Session session = sessionService.findSessionById(uuid).orElse(null);
        if (session == null || session.getExpiresAt().isBefore(LocalDateTime.now())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = session.getUser();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("user", user);

        filterChain.doFilter(request, response);
    }
}
