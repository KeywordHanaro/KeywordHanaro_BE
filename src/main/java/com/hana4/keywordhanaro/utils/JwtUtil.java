package com.hana4.keywordhanaro.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;
	private final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 100;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		if (secret != null) {
			this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
				Jwts.SIG.HS256.key().build().getAlgorithm());
		}
	}

	public String getUsername(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("username", String.class);
	}

	public Boolean isExpired(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	public String createJwt(String username) {
		return Jwts.builder()
			.claim("access", "access")
			.claim("username", username)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
			.signWith(secretKey)
			.compact();
	}
}
