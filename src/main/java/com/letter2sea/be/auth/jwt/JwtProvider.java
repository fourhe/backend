package com.letter2sea.be.auth.jwt;

import static com.letter2sea.be.auth.util.OAuthUtils.ACCESS_TOKEN;
import static com.letter2sea.be.auth.util.OAuthUtils.REFRESH_TOKEN;
import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_WEEK;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final String issuer;
    private final Algorithm algorithm;

    public JwtProvider(JwtProperties jwtProperties) {
        this.issuer = jwtProperties.getIssuer();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
    }

    public String issueAccessToken(Long memberId) {
        return issueToken(ACCESS_TOKEN, memberId, Date.from(Instant.now().plusMillis(ONE_HOUR)));
    }

    public String issueRefreshToken(Long memberId) {
        return issueToken(REFRESH_TOKEN, memberId, Date.from(Instant.now().plusMillis(ONE_WEEK)));
    }

    private String issueToken(String subject, Long memberId, Date expireAt) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withAudience(memberId.toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(expireAt)

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }
}
