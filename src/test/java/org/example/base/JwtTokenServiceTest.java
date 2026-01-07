package org.example.base;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.base.constants.Constant;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.Token;
import org.example.base.models.entity.user.User;
import org.example.base.services.token.ITokenStore;
import org.example.base.services.token.jwt.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private static final String SECRET = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

    private ITokenStore tokenStore;

    private JwtTokenService service;

    @BeforeEach
    void setUp() {
        tokenStore = mock(ITokenStore.class);
        service = new JwtTokenService(tokenStore);
        ReflectionTestUtils.setField(service, "tokenExpired", 60L); // seconds
        ReflectionTestUtils.setField(service, "secretKey", SECRET);
    }

    @Test
    void createToken_setsUserIdWhenMissingAndStoresNewToken() {
        // Arrange
        User user = buildUser(100L, "alice", Set.of("ROLE_USER"));
        TokenRequest req = new TokenRequest();
        when(tokenStore.getToken(any(TokenRequest.class))).thenReturn(null);

        // Act
        Token token = service.createToken(user, req);

        // Assert
        assertThat(req.getUserId()).isEqualTo(100L);
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isNotBlank();
        assertThat(token.getUserId()).isEqualTo(100L);
        assertThat(token.getUsername()).isEqualTo("alice");
        assertThat(token.getExpiredTime()).isGreaterThan(System.currentTimeMillis());
        assertThat(token.getAuthorities()).containsExactlyInAnyOrder("ROLE_USER");

        verify(tokenStore, times(1)).storeToken(any(Token.class), eq(req));
        verify(tokenStore, times(1)).getToken(req);
        verifyNoMoreInteractions(tokenStore);
    }

    @Test
    void createToken_returnsExistingTokenWhenNotExpired() {
        // Arrange
        User user = buildUser(200L, "bob", Set.of("ROLE_USER"));
        TokenRequest req = new TokenRequest();
        Token existing = new Token();
        existing.setUserId(200L);
        existing.setUsername("bob");
        existing.setToken("existing-token");
        existing.setExpiredTime(System.currentTimeMillis() + 60_000);
        when(tokenStore.getToken(any(TokenRequest.class))).thenReturn(existing);

        // Act
        Token result = service.createToken(user, req);

        // Assert
        assertThat(result).isSameAs(existing);
        verify(tokenStore, never()).removeToken(any());
        verify(tokenStore, never()).storeToken(any(), any());
        verify(tokenStore, times(1)).getToken(req);
        verifyNoMoreInteractions(tokenStore);
    }

    @Test
    void createToken_removesExpiredExistingTokenAndCreatesNew() {
        // Arrange
        User user = buildUser(300L, "charlie", Set.of("ROLE_ADMIN"));
        TokenRequest req = new TokenRequest();
        Token expired = new Token();
        expired.setUserId(300L);
        expired.setUsername("charlie");
        expired.setToken("expired-token");
        expired.setExpiredTime(System.currentTimeMillis() - 1);
        when(tokenStore.getToken(any(TokenRequest.class))).thenReturn(expired);

        // Act
        Token result = service.createToken(user, req);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isNotSameAs(expired);
        assertThat(result.getToken()).isNotBlank();
        assertThat(result.getExpiredTime()).isGreaterThan(System.currentTimeMillis());
        verify(tokenStore, times(1)).removeToken(expired);
        verify(tokenStore, times(1)).storeToken(any(Token.class), eq(req));
        verify(tokenStore, times(1)).getToken(req);
        verifyNoMoreInteractions(tokenStore);
    }

    @Test
    void getToken_delegatesToTokenStore() {
        // Arrange
        TokenRequest req = new TokenRequest();
        Token t = new Token();
        t.setToken("abc");
        when(tokenStore.getToken(req)).thenReturn(t);

        // Act
        Token result = service.getToken(req);

        // Assert
        assertThat(result).isSameAs(t);
        verify(tokenStore, times(1)).getToken(req);
        verifyNoMoreInteractions(tokenStore);
    }

    @Test
    void createToken_generatesJwtWithExpectedClaims() {
        // Arrange
        User user = buildUser(400L, "dana", Set.of("ROLE_USER", "READ"));
        TokenRequest req = new TokenRequest();
        when(tokenStore.getToken(any(TokenRequest.class))).thenReturn(null);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);

        // Act
        Token created = service.createToken(user, req);

        // Assert token fields
        assertThat(created.getUserId()).isEqualTo(400L);
        assertThat(created.getUsername()).isEqualTo("dana");
        assertThat(created.getAuthorities()).containsExactlyInAnyOrder("ROLE_USER", "READ");
        assertThat(created.getExpiredTime()).isGreaterThan(System.currentTimeMillis());
        assertThat(created.getToken()).isNotBlank();

        // Verify store interaction and capture stored token
        verify(tokenStore).storeToken(tokenCaptor.capture(), eq(req));
        Token stored = tokenCaptor.getValue();

        // Assert JWT claims based on stored token
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(stored.getToken())
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("dana");
        assertThat(claims.getExpiration()).isAfter(new Date());

        Number userIdClaim = (Number) claims.get(Constant.GrantTypeToken.JWT_USER_ID);
        assertThat(userIdClaim.longValue()).isEqualTo(400L);

        @SuppressWarnings("unchecked")
        List<String> scope = (List<String>) claims.get(Constant.GrantTypeToken.JWT_SCOPE);
        assertThat(scope).containsExactlyInAnyOrder("ROLE_USER", "READ");

        verify(tokenStore, times(1)).getToken(req);
        verifyNoMoreInteractions(tokenStore);
    }

    @Test
    void createToken_doesNotOverwriteProvidedUserIdInRequest() {
        // Arrange
        User user = buildUser(500L, "eve", Set.of("ROLE_USER"));
        TokenRequest req = new TokenRequest();
        req.setUserId(999L); // pre-set, should not be overwritten
        when(tokenStore.getToken(req)).thenReturn(null);

        // Act
        Token token = service.createToken(user, req);

        // Assert
        assertThat(req.getUserId()).isEqualTo(999L);
        assertThat(token.getUserId()).isEqualTo(500L); // token reflects real user id
        verify(tokenStore, times(1)).getToken(req);
        verify(tokenStore, times(1)).storeToken(any(Token.class), eq(req));
        verifyNoMoreInteractions(tokenStore);
    }

    private static User buildUser(Long id, String username, Set<String> authorities) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setAuthorities(new HashSet<>(authorities));
        return user;
    }
}
