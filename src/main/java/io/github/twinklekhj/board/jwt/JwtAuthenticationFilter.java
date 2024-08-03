package io.github.twinklekhj.board.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProperties tokenProperties;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 토큰이 필요하지 않은 API URL에 대해서 배열로 구성
        List<String> list = Arrays.asList(
                "/api/list",
                "/api/authenticate",
                "/api/register",
                "/api/csrf-token"
        );

        String uri = request.getRequestURI();
        if (list.contains(uri) || !uri.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getMethod().equals("GET") && uri.equals("/api/user/image")){
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("요청한 URL: {}", uri);

        Optional<String> jwtOptional = tokenProvider.resolveToken(request);
        if (jwtOptional.isPresent()) {
            String jwt = jwtOptional.get();
            try {
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("jwt 인증 완료: {}", authentication.getPrincipal());
                }

                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                log.error("ERROR: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot find bearer token");
        }
    }
}
