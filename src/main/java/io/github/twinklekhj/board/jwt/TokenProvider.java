package io.github.twinklekhj.board.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    private final TokenProperties tokenProperties;
    private final Key key;

    public TokenProvider(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
        SecretKey key = Keys.hmacShaKeyFor(tokenProperties.getSecretKey().getBytes());
        this.key = Keys.hmacShaKeyFor(key.getEncoded());
    }

    /**
     * [TokenProvider] Access Token 생성
     *
     * @param authentication 인증 객체
     * @return Access Token
     */
    public Token generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        Date now = new Date();
        Date tokenExpiresIn = new Date(now.getTime() + tokenProperties.getTokenExpireTime());

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(tokenProperties.getAuthorizeKey(), authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return Token.builder()
                .grantType(tokenProperties.getBearerType())
                .accessToken(accessToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .build();
    }

    /**
     * [TokenProvider] Access Token으로 인증 객체 가져오기
     *
     * @param accessToken Access Token
     * @return 인증 객체
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(tokenProperties.getAuthorizeKey()) == null) {
            throw new JwtException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(tokenProperties.getAuthorizeKey()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * [TokenProvider] Token 검증
     *
     * @param token 검증할 Token
     * @return 유효성 판단 유무
     */
    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 토큰이 잘못되었습니다.");
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * [TokenProvider] Request로 부터 Token 분리
     *
     * @param request 요청 객체
     * @return Token String
     */
    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenProperties.getAuthorizeHeader());
        if (bearerToken == null) {
            return Optional.empty();
        }
        String bearerType = tokenProperties.getBearerType();
        if (bearerToken.startsWith(bearerType)) {
            return Optional.of(bearerToken.substring(bearerType.length() + 1));
        }
        return Optional.empty();
    }
}
